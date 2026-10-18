package com.shopsphere.dao;

import com.shopsphere.model.Coupon;
import com.shopsphere.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for promotional Coupons and discount rules.
 */
public class CouponDAO {

    public Coupon findByCode(String code) throws SQLException {
        if (code == null) return null;
        String sql = "SELECT * FROM coupons WHERE UPPER(code) = UPPER(?) AND status = TRUE AND expiry_date >= CURRENT_DATE";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, code.trim());
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapCoupon(rs);
            }
            return null;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public List<Coupon> findAll() throws SQLException {
        List<Coupon> list = new ArrayList<>();
        String sql = "SELECT * FROM coupons ORDER BY coupon_id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapCoupon(rs));
            }
            return list;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public boolean create(Coupon coupon) throws SQLException {
        String sql = "INSERT INTO coupons (code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, coupon.getCode().toUpperCase().trim());
            ps.setString(2, coupon.getDiscountType());
            ps.setDouble(3, coupon.getDiscountValue());
            ps.setDouble(4, coupon.getMinimumOrder());
            ps.setDouble(5, coupon.getMaximumDiscount());
            ps.setDate(6, coupon.getExpiryDate());
            ps.setBoolean(7, coupon.isStatus());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    coupon.setCouponId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public boolean delete(int couponId) throws SQLException {
        String sql = "DELETE FROM coupons WHERE coupon_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, couponId);
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    public boolean recordUsage(int couponId, int userId, int orderId, Connection conn) throws SQLException {
        String sql = "INSERT INTO coupon_usage (coupon_id, user_id, order_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            ps.setInt(3, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean isUsedByUser(int couponId, int userId) throws SQLException {
        String sql = "SELECT usage_id FROM coupon_usage WHERE coupon_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            rs = ps.executeQuery();
            return rs.next();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    private Coupon mapCoupon(ResultSet rs) throws SQLException {
        return new Coupon(
                rs.getInt("coupon_id"),
                rs.getString("code"),
                rs.getString("discount_type"),
                rs.getDouble("discount_value"),
                rs.getDouble("minimum_order"),
                rs.getDouble("maximum_discount"),
                rs.getDate("expiry_date"),
                rs.getBoolean("status")
        );
    }
}
