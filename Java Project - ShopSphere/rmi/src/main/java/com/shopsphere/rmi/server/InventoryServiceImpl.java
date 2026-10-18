package com.shopsphere.rmi.server;

import com.shopsphere.rmi.remote.InventoryService;
import com.shopsphere.rmi.remote.ProductDTO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Remote Implementation of the ShopSphere RMI Inventory Service.
 */
public class InventoryServiceImpl extends UnicastRemoteObject implements InventoryService {

    private static final long serialVersionUID = 1L;
    private final Map<Integer, ProductDTO> localCache = new HashMap<>();

    public InventoryServiceImpl() throws RemoteException {
        super();
        localCache.put(1, new ProductDTO(1, "ShopSphere ProBook 15 G4", "SphereTech", 74999.00, 25));
        localCache.put(2, new ProductDTO(2, "Aura ANC Wireless Headphones", "SoundSphere", 6499.00, 50));
        localCache.put(3, new ProductDTO(3, "UltraVision 4K OLED Smart Monitor", "SphereTech", 39999.00, 15));
        localCache.put(4, new ProductDTO(4, "NeoTrack Pro Smartwatch", "AeroFit", 4499.00, 80));
    }

    @Override
    public ProductDTO getProduct(int id) throws RemoteException {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopsphere?useSSL=false&serverTimezone=UTC", "root", "Project@1221");
             PreparedStatement ps = conn.prepareStatement("SELECT product_id, name, brand, price, stock FROM products WHERE product_id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ProductDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getInt(5));
                }
            }
        } catch (Exception ignored) {}
        return localCache.get(id);
    }

    @Override
    public int getStock(int productId) throws RemoteException {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopsphere?useSSL=false&serverTimezone=UTC", "root", "Project@1221");
             PreparedStatement ps = conn.prepareStatement("SELECT stock FROM products WHERE product_id = ?")) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception ignored) {}
        ProductDTO p = localCache.get(productId);
        return p != null ? p.getStock() : 0;
    }

    @Override
    public boolean updateStock(int productId, int quantity) throws RemoteException {
        int currentStock = getStock(productId);
        if (currentStock + quantity < 0) {
            System.err.println("[RMI Server] Stock rejection: Cannot update product #" + productId + " to negative stock.");
            return false;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopsphere?useSSL=false&serverTimezone=UTC", "root", "Project@1221");
             PreparedStatement ps = conn.prepareStatement("UPDATE products SET stock = stock + ? WHERE product_id = ?")) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            if (ps.executeUpdate() > 0) return true;
        } catch (Exception ignored) {}

        ProductDTO p = localCache.get(productId);
        if (p != null) {
            p.setStock(currentStock + quantity);
            return true;
        }
        return false;
    }
}
