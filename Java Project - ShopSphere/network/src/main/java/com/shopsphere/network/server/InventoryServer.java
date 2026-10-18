package com.shopsphere.network.server;

import com.shopsphere.network.model.ProductDTO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Multi-threaded Inventory Server using java.net ServerSocket and Object Serialization.
 */
public class InventoryServer {

    public static final int PORT = 8888;
    private static final Map<Integer, ProductDTO> fallbackInventory = new HashMap<>();

    static {
        fallbackInventory.put(1, new ProductDTO(1, "ShopSphere ProBook 15 G4", "SphereTech", 74999.00, 25));
        fallbackInventory.put(2, new ProductDTO(2, "Aura ANC Wireless Headphones", "SoundSphere", 6499.00, 50));
        fallbackInventory.put(3, new ProductDTO(3, "UltraVision 4K OLED Smart Monitor", "SphereTech", 39999.00, 15));
        fallbackInventory.put(4, new ProductDTO(4, "NeoTrack Pro Smartwatch", "AeroFit", 4499.00, 80));
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  ShopSphere Java Socket Inventory Server");
        System.out.println("  Listening on TCP Port: " + PORT);
        System.out.println("==================================================");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[Server] Accepted connection from: " + clientSocket.getRemoteSocketAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("[Server] Fatal ServerSocket error: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())
            ) {
                out.println("OK Connected to ShopSphere Inventory Socket Server (v1.0)");

                String line;
                while ((line = in.readLine()) != null) {
                    line = line.trim();
                    if ("QUIT".equalsIgnoreCase(line) || "EXIT".equalsIgnoreCase(line)) {
                        out.println("BYE Disconnecting session");
                        break;
                    }

                    if (line.startsWith("GET_STOCK ")) {
                        try {
                            int id = Integer.parseInt(line.substring(10).trim());
                            int stock = queryStock(id);
                            out.println("STOCK " + id + ":" + stock);
                        } catch (NumberFormatException e) {
                            out.println("ERR Invalid product ID format");
                        }
                    } else if (line.startsWith("GET_PRODUCT ")) {
                        try {
                            int id = Integer.parseInt(line.substring(12).trim());
                            ProductDTO dto = queryProduct(id);
                            if (dto != null) {
                                out.println("OBJECT_FOLLOWING");
                                oos.writeObject(dto);
                                oos.flush();
                            } else {
                                out.println("NOT_FOUND");
                            }
                        } catch (NumberFormatException e) {
                            out.println("ERR Invalid product ID format");
                        }
                    } else if (line.startsWith("SEARCH_PRODUCT ")) {
                        String query = line.substring(15).trim().toLowerCase();
                        List<ProductDTO> results = searchProducts(query);
                        out.println("MATCH_COUNT " + results.size());
                        for (ProductDTO p : results) {
                            out.println("ITEM " + p.getProductId() + " | " + p.getName() + " | ₹" + p.getPrice() + " | Stock: " + p.getStock());
                        }
                    } else {
                        out.println("ERR Unknown command. Supported: GET_STOCK <id>, GET_PRODUCT <id>, SEARCH_PRODUCT <q>, QUIT");
                    }
                }
            } catch (IOException e) {
                System.out.println("[Server] Client session closed: " + e.getMessage());
            } finally {
                try { socket.close(); } catch (IOException ignored) {}
            }
        }

        private int queryStock(int id) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopsphere?useSSL=false&serverTimezone=UTC", "root", "Project@1221");
                 PreparedStatement ps = conn.prepareStatement("SELECT stock FROM products WHERE product_id = ?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt("stock");
                }
            } catch (Exception ignored) {}
            return fallbackInventory.containsKey(id) ? fallbackInventory.get(id).getStock() : 0;
        }

        private ProductDTO queryProduct(int id) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopsphere?useSSL=false&serverTimezone=UTC", "root", "Project@1221");
                 PreparedStatement ps = conn.prepareStatement("SELECT product_id, name, brand, price, stock FROM products WHERE product_id = ?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new ProductDTO(rs.getInt("product_id"), rs.getString("name"), rs.getString("brand"), rs.getDouble("price"), rs.getInt("stock"));
                    }
                }
            } catch (Exception ignored) {}
            return fallbackInventory.get(id);
        }

        private List<ProductDTO> searchProducts(String query) {
            List<ProductDTO> list = new ArrayList<>();
            for (ProductDTO p : fallbackInventory.values()) {
                if (p.getName().toLowerCase().contains(query) || p.getBrand().toLowerCase().contains(query)) {
                    list.add(p);
                }
            }
            return list;
        }
    }
}
