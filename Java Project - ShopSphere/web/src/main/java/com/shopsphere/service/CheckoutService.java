package com.shopsphere.service;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.OrderDAO;
import com.shopsphere.model.Address;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.Order;

import java.sql.SQLException;
import java.util.List;

/**
 * Service orchestrating order calculations, coupon validation, stock validation,
 * and final transactional checkout placement.
 */
public class CheckoutService {

    private final CartDAO cartDAO;
    private final OrderDAO orderDAO;
    private final AddressDAO addressDAO;
    private final CouponService couponService;

    public CheckoutService() {
        this.cartDAO = new CartDAO();
        this.orderDAO = new OrderDAO();
        this.addressDAO = new AddressDAO();
        this.couponService = new CouponService();
    }

    public static class CheckoutCalculation {
        private double subtotal;
        private double couponDiscount;
        private double shippingCharge;
        private double grandTotal;
        private Coupon appliedCoupon;
        private String couponMessage;

        public double getSubtotal() { return subtotal; }
        public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

        public double getCouponDiscount() { return couponDiscount; }
        public void setCouponDiscount(double couponDiscount) { this.couponDiscount = couponDiscount; }

        public double getShippingCharge() { return shippingCharge; }
        public void setShippingCharge(double shippingCharge) { this.shippingCharge = shippingCharge; }

        public double getGrandTotal() { return grandTotal; }
        public void setGrandTotal(double grandTotal) { this.grandTotal = grandTotal; }

        public Coupon getAppliedCoupon() { return appliedCoupon; }
        public void setAppliedCoupon(Coupon appliedCoupon) { this.appliedCoupon = appliedCoupon; }

        public String getCouponMessage() { return couponMessage; }
        public void setCouponMessage(String couponMessage) { this.couponMessage = couponMessage; }
    }

    /**
     * Calculates order pricing strictly on the server side to eliminate tampering.
     */
    public CheckoutCalculation calculateOrder(List<CartItem> cartItems, String couponCode, int userId) throws SQLException {
        CheckoutCalculation calc = new CheckoutCalculation();
        double subtotal = 0.0;

        if (cartItems != null) {
            for (CartItem item : cartItems) {
                subtotal += item.getItemTotal();
            }
        }
        subtotal = Math.round(subtotal * 100.0) / 100.0;
        calc.setSubtotal(subtotal);

        // Free shipping on orders over ₹999, else standard ₹99
        double shipping = (subtotal >= 999.0 || subtotal == 0) ? 0.0 : 99.0;
        calc.setShippingCharge(shipping);

        double discount = 0.0;
        if (couponCode != null && !couponCode.trim().isEmpty() && subtotal > 0) {
            try {
                Coupon c = couponService.validateCoupon(couponCode, subtotal, userId);
                discount = c.calculateDiscount(subtotal);
                calc.setAppliedCoupon(c);
                calc.setCouponMessage("Coupon " + c.getCode() + " applied successfully! Saved ₹" + discount);
            } catch (Exception ex) {
                calc.setCouponMessage("Coupon Error: " + ex.getMessage());
            }
        }
        calc.setCouponDiscount(discount);

        double total = Math.max(0.0, subtotal - discount + shipping);
        calc.setGrandTotal(Math.round(total * 100.0) / 100.0);
        return calc;
    }

    /**
     * Executes end-to-end checkout.
     */
    public Order placeOrder(int userId, int addressId, String paymentMethod, String couponCode) throws SQLException {
        List<CartItem> cartItems = cartDAO.getCartItems(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Your cart is empty. Please add items before checking out.");
        }

        Address address = addressDAO.findById(addressId);
        if (address == null || address.getUserId() != userId) {
            throw new IllegalArgumentException("Invalid shipping address selected");
        }

        // Validate stock availability on server
        for (CartItem ci : cartItems) {
            if (ci.getProduct() == null || !ci.getProduct().isStatus()) {
                throw new IllegalStateException("Product '" + (ci.getProduct() != null ? ci.getProduct().getName() : "Unknown") + "' is no longer available.");
            }
            if (ci.getProduct().getStock() < ci.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for '" + ci.getProduct().getName() + "'. Available: " + ci.getProduct().getStock());
            }
        }

        // Calculate secure prices
        CheckoutCalculation calc = calculateOrder(cartItems, couponCode, userId);

        Order order = new Order();
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setTotalAmount(calc.getGrandTotal());
        order.setDiscount(calc.getCouponDiscount());
        order.setPaymentMethod(paymentMethod != null ? paymentMethod : "CREDIT_CARD");
        order.setPaymentStatus("PAID"); // Simulated Gateway success
        order.setOrderStatus("PLACED");

        Integer couponId = (calc.getAppliedCoupon() != null) ? calc.getAppliedCoupon().getCouponId() : null;

        boolean success = orderDAO.placeOrder(order, cartItems, couponId);
        if (success) {
            order.setAddress(address);
            return order;
        }
        throw new SQLException("Checkout could not be completed. Please try again.");
    }
}
