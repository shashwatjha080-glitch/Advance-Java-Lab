package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Represents a shopping cart line item associated with a user and a product.
 */
public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int cartId;
    private int userId;
    private int productId;
    private int quantity;
    private Timestamp createdAt;
    private Product product; // Populated from product join

    public CartItem() {}

    public CartItem(int cartId, int userId, int productId, int quantity, Timestamp createdAt) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getItemTotal() {
        if (product != null) {
            return Math.round((product.getDiscountedPrice() * quantity) * 100.0) / 100.0;
        }
        return 0.0;
    }

    public double getOriginalItemTotal() {
        if (product != null) {
            return Math.round((product.getPrice() * quantity) * 100.0) / 100.0;
        }
        return 0.0;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
