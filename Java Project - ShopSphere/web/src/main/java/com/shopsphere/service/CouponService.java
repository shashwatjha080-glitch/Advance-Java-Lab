package com.shopsphere.service;

import com.shopsphere.dao.CouponDAO;
import com.shopsphere.model.Coupon;

import java.sql.SQLException;
import java.util.List;

/**
 * Service managing coupon redemption, validation and administrative operations.
 */
public class CouponService {

    private final CouponDAO couponDAO;

    public CouponService() {
        this.couponDAO = new CouponDAO();
    }

    public CouponService(CouponDAO couponDAO) {
        this.couponDAO = couponDAO;
    }

    /**
     * Validates if a coupon code can be applied to an order amount by a specific user.
     */
    public Coupon validateCoupon(String code, double orderAmount, int userId) throws SQLException, IllegalArgumentException {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Coupon code cannot be empty");
        }

        Coupon coupon = couponDAO.findByCode(code.trim());
        if (coupon == null) {
            throw new IllegalArgumentException("Invalid or expired coupon code: " + code);
        }

        if (!coupon.isStatus()) {
            throw new IllegalArgumentException("This coupon is no longer active");
        }

        if (coupon.isExpired()) {
            throw new IllegalArgumentException("This coupon expired on " + coupon.getExpiryDate());
        }

        if (orderAmount < coupon.getMinimumOrder()) {
            throw new IllegalArgumentException(String.format("Minimum order amount of ₹%.2f required to use this coupon", coupon.getMinimumOrder()));
        }

        if (couponDAO.isUsedByUser(coupon.getCouponId(), userId)) {
            throw new IllegalArgumentException("You have already redeemed this coupon code once");
        }

        return coupon;
    }

    public List<Coupon> getAllCoupons() throws SQLException {
        return couponDAO.findAll();
    }

    public boolean createCoupon(Coupon coupon) throws SQLException {
        return couponDAO.create(coupon);
    }

    public boolean deleteCoupon(int couponId) throws SQLException {
        return couponDAO.delete(couponId);
    }
}
