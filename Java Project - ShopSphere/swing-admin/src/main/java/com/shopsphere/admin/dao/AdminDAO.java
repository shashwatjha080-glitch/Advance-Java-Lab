package com.shopsphere.admin.dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Enterprise Swing Desktop Data Access Object.
 */
public class AdminDAO {

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean authenticateAdmin(String email, String password) throws SQLException {
        String sql = "SELECT password, role FROM users WHERE email = ? AND status = 'ACTIVE'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = AdminDBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email.trim().toLowerCase());
            rs = ps.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                String role = rs.getString("role");
                if ("ADMIN".equalsIgnoreCase(role)) {
                    String inputHash = hashPassword(password);
                    return inputHash.equalsIgnoreCase(storedHash);
                }
            }
            return false;
        } finally {
            AdminDBUtil.close(rs, ps, conn);
        }
    }

    public Map<String, Object> getDashboardStats() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = AdminDBUtil.getConnection();

            // Total Revenue
            ps = conn.prepareStatement("SELECT COALESCE(SUM(total_amount), 0.0) FROM orders WHERE payment_status = 'PAID'");
            rs = ps.executeQuery();
            stats.put("revenue", rs.next() ? rs.getDouble(1) : 0.0);
            rs.close(); ps.close();

            // Total Orders
            ps = conn.prepareStatement("SELECT COUNT(*) FROM orders");
            rs = ps.executeQuery();
            stats.put("orders", rs.next() ? rs.getInt(1) : 0);
            rs.close(); ps.close();

            // Customers
            ps = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE role = 'CUSTOMER'");
            rs = ps.executeQuery();
            stats.put("customers", rs.next() ? rs.getInt(1) : 0);
            rs.close(); ps.close();

            // Low Stock
            ps = conn.prepareStatement("SELECT COUNT(*) FROM products WHERE stock <= 10");
            rs = ps.executeQuery();
            stats.put("lowStock", rs.next() ? rs.getInt(1) : 0);

            return stats;
        } finally {
            AdminDBUtil.close(rs, ps, conn);
        }
    }

    public Vector<Vector<Object>> getProductsTableData() throws SQLException {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT p.product_id, p.name, p.brand, c.name AS cat_name, p.price, p.discount, p.stock, p.status " +
                     "FROM products p LEFT JOIN categories c ON p.category_id = c.category_id ORDER BY p.product_id ASC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = AdminDBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("product_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("brand"));
                row.add(rs.getString("cat_name"));
                row.add(rs.getDouble("price"));
                row.add(rs.getDouble("discount"));
                row.add(rs.getInt("stock"));
                row.add(rs.getBoolean("status") ? "Active" : "Disabled");
                data.add(row);
            }
            return data;
        } finally {
            AdminDBUtil.close(rs, ps, conn);
        }
    }

    public boolean saveProduct(Integer id, int categoryId, String name, String brand, String desc, double price, double discount, int stock, String imgUrl, boolean status) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = AdminDBUtil.getConnection();
            if (id == null || id <= 0) {
                String sql = "INSERT INTO products (category_id, name, brand, description, price, discount, stock, image_url, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, categoryId);
                ps.setString(2, name);
                ps.setString(3, brand);
                ps.setString(4, desc);
                ps.setDouble(5, price);
                ps.setDouble(6, discount);
                ps.setInt(7, stock);
                ps.setString(8, imgUrl);
                ps.setBoolean(9, status);
            } else {
                String sql = "UPDATE products SET category_id = ?, name = ?, brand = ?, description = ?, price = ?, discount = ?, stock = ?, image_url = ?, status = ? WHERE product_id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, categoryId);
                ps.setString(2, name);
                ps.setString(3, brand);
                ps.setString(4, desc);
                ps.setDouble(5, price);
                ps.setDouble(6, discount);
                ps.setInt(7, stock);
                ps.setString(8, imgUrl);
                ps.setBoolean(9, status);
                ps.setInt(10, id);
            }
            return ps.executeUpdate() > 0;
        } finally {
            AdminDBUtil.close(ps, conn);
        }
    }

    public boolean deleteProduct(int productId) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = AdminDBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } finally {
            AdminDBUtil.close(ps, conn);
        }
    }

    public Vector<Vector<Object>> getCategoriesTableData() throws SQLException {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT * FROM categories ORDER BY category_id ASC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = AdminDBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("category_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("description"));
                row.add(rs.getBoolean("status") ? "Active" : "Disabled");
                data.add(row);
            }
            return data;
        } finally {
            AdminDBUtil.close(rs, ps, conn);
        }
    }

    public boolean saveCategory(Integer id, String name, String desc, boolean status) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = AdminDBUtil.getConnection();
            if (id == null || id <= 0) {
                String sql = "INSERT INTO categories (name, description, status) VALUES (?, ?, ?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, desc);
                ps.setBoolean(3, status);
            } else {
                String sql = "UPDATE categories SET name = ?, description = ?, status = ? WHERE category_id = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, desc);
                ps.setBoolean(3, status);
                ps.setInt(4, id);
            }
            return ps.executeUpdate() > 0;
        } finally {
            AdminDBUtil.close(ps, conn);
        }
    }

    public boolean deleteCategory(int id) throws SQLException {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = AdminDBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } finally {
            AdminDBUtil.close(ps, conn);
        }
    }

    public Vector<Vector<Object>> getOrdersTableData() throws SQLException {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT o.order_id, u.name, o.total_amount, o.payment_method, o.payment_status, o.order_status, o.created_at " +
                     "FROM orders o JOIN users u ON o.user_id = u.user_id ORDER BY o.order_id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = AdminDBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("order_id"));
                row.add(rs.getString("name"));
                row.add(rs.getDouble("total_amount"));
                row.add(rs.getString("payment_method"));
                row.add(rs.getString("payment_status"));
                row.add(rs.getString("order_status"));
                row.add(rs.getTimestamp("created_at"));
                data.add(row);
            }
            return data;
        } finally {
            AdminDBUtil.close(rs, ps, conn);
        }
    }

    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = AdminDBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } finally {
            AdminDBUtil.close(ps, conn);
        }
    }

    public Vector<Vector<Object>> getCustomersTableData() throws SQLException {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT user_id, name, email, mobile, status, created_at FROM users WHERE role = 'CUSTOMER' ORDER BY user_id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = AdminDBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("user_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("email"));
                row.add(rs.getString("mobile"));
                row.add(rs.getString("status"));
                row.add(rs.getTimestamp("created_at"));
                data.add(row);
            }
            return data;
        } finally {
            AdminDBUtil.close(rs, ps, conn);
        }
    }

    public boolean updateUserStatus(int userId, String status) throws SQLException {
        String sql = "UPDATE users SET status = ? WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = AdminDBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } finally {
            AdminDBUtil.close(ps, conn);
        }
    }

    public Vector<Vector<Object>> getInventoryTableData(int threshold) throws SQLException {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT product_id, name, brand, stock FROM products WHERE stock <= ? ORDER BY stock ASC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = AdminDBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, threshold);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("product_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("brand"));
                row.add(rs.getInt("stock"));
                data.add(row);
            }
            return data;
        } finally {
            AdminDBUtil.close(rs, ps, conn);
        }
    }

    public boolean updateStock(int productId, int newStock) throws SQLException {
        String sql = "UPDATE products SET stock = ? WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = AdminDBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } finally {
            AdminDBUtil.close(ps, conn);
        }
    }
}
