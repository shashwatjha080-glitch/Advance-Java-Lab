package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer purchase order.
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private int orderId;
    private int userId;
    private Integer addressId;
    private double totalAmount;
    private double discount;
    private String paymentMethod; // 'CREDIT_CARD', 'UPI', 'NET_BANKING', 'COD'
    private String paymentStatus; // 'PAID', 'PENDING', 'FAILED'
    private String orderStatus;   // 'PLACED', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED'
    private Timestamp createdAt;

    // Associated entities for rich presentation
    private String userName;
    private String userEmail;
    private Address address;
    private List<OrderItem> items = new ArrayList<>();

    public Order() {}

    public Order(int orderId, int userId, Integer addressId, double totalAmount, double discount,
                 String paymentMethod, String paymentStatus, String orderStatus, Timestamp createdAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.addressId = addressId;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public int getTotalQuantity() {
        int qty = 0;
        if (items != null) {
            for (OrderItem it : items) {
                qty += it.getQuantity();
            }
        }
        return qty;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", totalAmount=" + totalAmount +
                ", orderStatus='" + orderStatus + '\'' +
                '}';
    }
}
