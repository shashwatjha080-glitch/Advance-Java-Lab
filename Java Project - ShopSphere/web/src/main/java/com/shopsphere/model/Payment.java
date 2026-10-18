package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Represents a payment transaction (Simulated Gateway).
 */
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    private int paymentId;
    private int orderId;
    private String paymentMethod;
    private String transactionReference;
    private double amount;
    private String paymentStatus; // 'SUCCESS', 'FAILED', 'PENDING'
    private Timestamp createdAt;

    public Payment() {}

    public Payment(int paymentId, int orderId, String paymentMethod, String transactionReference, double amount, String paymentStatus, Timestamp createdAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.transactionReference = transactionReference;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
