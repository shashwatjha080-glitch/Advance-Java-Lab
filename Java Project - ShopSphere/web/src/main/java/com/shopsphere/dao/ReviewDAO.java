package com.shopsphere.dao;

import com.shopsphere.model.Review;
import com.shopsphere.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for customer reviews.
 */
public class ReviewDAO {

    public boolean addReview(Review review) throws SQLException {
        String sql = "INSERT INTO reviews (user_id, product_id, rating, review_text) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getProductId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getReviewText());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    review.setReviewId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public List<Review> findByProductId(int productId) throws SQLException {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.*, u.name AS user_name FROM reviews r " +
                     "JOIN users u ON r.user_id = u.user_id " +
                     "WHERE r.product_id = ? ORDER BY r.review_id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Review r = new Review(
                        rs.getInt("review_id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id"),
                        rs.getInt("rating"),
                        rs.getString("review_text"),
                        rs.getTimestamp("created_at")
                );
                r.setUserName(rs.getString("user_name"));
                list.add(r);
            }
            return list;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public double getAverageRating(int productId) throws SQLException {
        String sql = "SELECT COALESCE(AVG(rating), 0.0) FROM reviews WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return Math.round(rs.getDouble(1) * 10.0) / 10.0;
            }
            return 0.0;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }
}
