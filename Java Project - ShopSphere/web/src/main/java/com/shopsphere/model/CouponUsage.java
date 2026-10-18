package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Audit entity tracking usage of coupon codes per user and order.
 */
public class CouponUsage implements Serializable {
    private static final long serialVersionUID = 1L;

    private int usageId;
    private int couponId;
    private int userId;
    private int orderId;
    private Timestamp usedAt;

    public CouponUsage() {}

    public CouponUsage(int usageId, int couponId, int userId, int orderId, Timestamp usedAt) {
        this.usageId = usageId;
        this.couponId = couponId;
        this.userId = userId;
        this.orderId = orderId;
        this.usedAt = usedAt;
    }

    public int getUsageId() {
        return usageId;
    }

    public void setUsageId(int usageId) {
        this.usageId = usageId;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Timestamp getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(Timestamp usedAt) {
        this.usedAt = usedAt;
    }
}
