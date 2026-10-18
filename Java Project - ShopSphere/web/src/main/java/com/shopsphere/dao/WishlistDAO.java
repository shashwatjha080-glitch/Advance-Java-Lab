package com.shopsphere.dao;

import com.shopsphere.model.Product;
import com.shopsphere.model.WishlistItem;
import com.shopsphere.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Customer Wishlist.
 */
public class WishlistDAO {

    public boolean addToWishlist(int userId, int productId) throws SQLException {
        String sql = "INSERT IGNORE INTO wishlist (user_id, product_id) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    public boolean removeFromWishlist(int userId, int productId) throws SQLException {
        String sql = "DELETE FROM wishlist WHERE user_id = ? AND product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    public boolean isInWishlist(int userId, int productId) throws SQLException {
        String sql = "SELECT wishlist_id FROM wishlist WHERE user_id = ? AND product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            rs = ps.executeQuery();
            return rs.next();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public List<WishlistItem> getWishlistItems(int userId) throws SQLException {
        List<WishlistItem> list = new ArrayList<>();
        String sql = "SELECT w.*, p.name AS product_name, p.brand AS product_brand, p.price, p.discount, " +
                     "p.stock, p.image_url, p.status AS product_status, c.name AS category_name " +
                     "FROM wishlist w " +
                     "JOIN products p ON w.product_id = p.product_id " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE w.user_id = ? ORDER BY w.wishlist_id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                WishlistItem item = new WishlistItem(
                        rs.getInt("wishlist_id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id"),
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
}
