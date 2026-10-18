package com.shopsphere.dao;

import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;
import com.shopsphere.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Shopping Cart.
 */
public class CartDAO {

    public boolean addToCart(int userId, int productId, int quantity) throws SQLException {
        String checkSql = "SELECT cart_id, quantity FROM cart WHERE user_id = ? AND product_id = ?";
        String updateSql = "UPDATE cart SET quantity = quantity + ? WHERE cart_id = ?";
        String insertSql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(checkSql);
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            rs = ps.executeQuery();
            if (rs.next()) {
                int cartId = rs.getInt("cart_id");
                rs.close();
                ps.close();

                ps = conn.prepareStatement(updateSql);
                ps.setInt(1, quantity);
                ps.setInt(2, cartId);
                return ps.executeUpdate() > 0;
            } else {
                rs.close();
                ps.close();

                ps = conn.prepareStatement(insertSql);
                ps.setInt(1, userId);
                ps.setInt(2, productId);
                ps.setInt(3, quantity > 0 ? quantity : 1);
                return ps.executeUpdate() > 0;
            }
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public boolean updateQuantity(int cartId, int userId, int quantity) throws SQLException {
        if (quantity <= 0) {
            return removeFromCart(cartId, userId);
        }
        String sql = "UPDATE cart SET quantity = ? WHERE cart_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, quantity);
            ps.setInt(2, cartId);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    public boolean removeFromCart(int cartId, int userId) throws SQLException {
        String sql = "DELETE FROM cart WHERE cart_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cartId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    public boolean clearCart(int userId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            return ps.executeUpdate() >= 0;
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    public boolean clearCart(int userId, Connection conn) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() >= 0;
        }
    }

    public List<CartItem> getCartItems(int userId) throws SQLException {
        List<CartItem> list = new ArrayList<>();
        String sql = "SELECT c.*, p.name AS product_name, p.brand AS product_brand, p.price, p.discount, " +
                     "p.stock, p.image_url, p.status AS product_status, cat.name AS category_name " +
                     "FROM cart c " +
                     "JOIN products p ON c.product_id = p.product_id " +
                     "LEFT JOIN categories cat ON p.category_id = cat.category_id " +
                     "WHERE c.user_id = ? ORDER BY c.cart_id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem(
                        rs.getInt("cart_id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getTimestamp("created_at")
                );

                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("product_name"));
                p.setBrand(rs.getString("product_brand"));
                p.setPrice(rs.getDouble("price"));
                p.setDiscount(rs.getDouble("discount"));
                p.setStock(rs.getInt("stock"));
                p.setImageUrl(rs.getString("image_url"));
                p.setStatus(rs.getBoolean("product_status"));
                p.setCategoryName(rs.getString("category_name"));

                item.setProduct(p);
                list.add(item);
            }
            return list;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public int getCartItemCount(int userId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM cart WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }
}
