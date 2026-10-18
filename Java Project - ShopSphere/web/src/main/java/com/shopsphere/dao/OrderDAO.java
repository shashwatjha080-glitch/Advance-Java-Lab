package com.shopsphere.dao;

import com.shopsphere.model.Address;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Order lifecycle and transaction management.
 */
public class OrderDAO {

    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final CartDAO cartDAO = new CartDAO();
    private final CouponDAO couponDAO = new CouponDAO();

    /**
     * Executes atomic transactional order placement:
     * 1. Stock validation & reduction
     * 2. Order header creation
     * 3. Order line items creation
     * 4. Payment record creation
     * 5. Coupon audit logging (if coupon used)
     * 6. Shopping cart cleanup
     */
    public boolean placeOrder(Order order, List<CartItem> cartItems, Integer couponId) throws SQLException {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cannot place order with empty cart");
        }

        Connection conn = null;
        PreparedStatement psOrder = null;
        PreparedStatement psItem = null;
        PreparedStatement psPayment = null;
        ResultSet rsOrder = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // Begin ACID Transaction

            // Step 1: Validate and deduct stock for every item
            for (CartItem ci : cartItems) {
                boolean stockReduced = productDAO.updateStock(ci.getProductId(), -ci.getQuantity(), conn);
                if (!stockReduced) {
                    conn.rollback();
                    throw new SQLException("Insufficient stock available for product ID: " + ci.getProductId());
                }
            }

            // Step 2: Insert into orders table
            String sqlOrder = "INSERT INTO orders (user_id, address_id, total_amount, discount, payment_method, payment_status, order_status) " +
                              "VALUES (?, ?, ?, ?, ?, ?, ?)";
            psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, order.getUserId());
            if (order.getAddressId() != null) {
                psOrder.setInt(2, order.getAddressId());
            } else {
                psOrder.setNull(2, java.sql.Types.INTEGER);
            }
            psOrder.setDouble(3, order.getTotalAmount());
            psOrder.setDouble(4, order.getDiscount());
            psOrder.setString(5, order.getPaymentMethod());
            psOrder.setString(6, order.getPaymentStatus());
            psOrder.setString(7, order.getOrderStatus() != null ? order.getOrderStatus() : "PLACED");

            int affected = psOrder.executeUpdate();
            if (affected == 0) {
                conn.rollback();
                return false;
            }

            rsOrder = psOrder.getGeneratedKeys();
            if (!rsOrder.next()) {
                conn.rollback();
                return false;
            }
            int orderId = rsOrder.getInt(1);
            order.setOrderId(orderId);

            // Step 3: Insert order_items
            String sqlItem = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            psItem = conn.prepareStatement(sqlItem);
            for (CartItem ci : cartItems) {
                psItem.setInt(1, orderId);
                psItem.setInt(2, ci.getProductId());
                psItem.setInt(3, ci.getQuantity());
                psItem.setDouble(4, ci.getProduct().getDiscountedPrice());
                psItem.addBatch();
            }
            psItem.executeBatch();

            // Step 4: Insert mock payment record
            String sqlPayment = "INSERT INTO payments (order_id, payment_method, transaction_reference, amount, payment_status) " +
                                "VALUES (?, ?, ?, ?, ?)";
            psPayment = conn.prepareStatement(sqlPayment);
            psPayment.setInt(1, orderId);
            psPayment.setString(2, order.getPaymentMethod());
            String txnRef = "TXN_SP_" + System.currentTimeMillis() + "_" + orderId;
            psPayment.setString(3, txnRef);
            psPayment.setDouble(4, order.getTotalAmount());
            psPayment.setString(5, "SUCCESS".equalsIgnoreCase(order.getPaymentStatus()) ? "SUCCESS" : "PENDING");
            psPayment.executeUpdate();

            // Step 5: Record coupon usage if applied
            if (couponId != null && couponId > 0) {
                couponDAO.recordUsage(couponId, order.getUserId(), orderId, conn);
            }

            // Step 6: Clear customer cart
            cartDAO.clearCart(order.getUserId(), conn);

            conn.commit(); // Commit ACID Transaction
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("[OrderDAO] Rollback failed: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignored) {}
            }
            DBUtil.close(rsOrder, psOrder, null);
            if (psItem != null) {
                try { psItem.close(); } catch (SQLException ignored) {}
            }
            if (psPayment != null) {
                try { psPayment.close(); } catch (SQLException ignored) {}
            }
            DBUtil.close(conn);
        }
    }

    public Order findById(int orderId) throws SQLException {
        String sql = "SELECT o.*, u.name AS user_name, u.email AS user_email, " +
                     "a.address_line, a.city, a.state, a.pincode, a.address_type " +
                     "FROM orders o " +
                     "JOIN users u ON o.user_id = u.user_id " +
                     "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                     "WHERE o.order_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Order order = mapOrder(rs);
                order.setItems(orderItemDAO.findByOrderId(orderId, conn));
                return order;
            }
            return null;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public List<Order> findByUserId(int userId) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.name AS user_name, u.email AS user_email, " +
                     "a.address_line, a.city, a.state, a.pincode, a.address_type " +
                     "FROM orders o " +
                     "JOIN users u ON o.user_id = u.user_id " +
                     "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                     "WHERE o.user_id = ? ORDER BY o.order_id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Order order = mapOrder(rs);
                list.add(order);
            }
            for (Order o : list) {
                o.setItems(orderItemDAO.findByOrderId(o.getOrderId(), conn));
            }
            return list;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public List<Order> findAll() throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.name AS user_name, u.email AS user_email, " +
                     "a.address_line, a.city, a.state, a.pincode, a.address_type " +
                     "FROM orders o " +
                     "JOIN users u ON o.user_id = u.user_id " +
                     "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                     "ORDER BY o.order_id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapOrder(rs));
            }
            return list;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    public boolean updatePaymentStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET payment_status = ? WHERE order_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    public double getTotalRevenue() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0.0) FROM orders WHERE payment_status = 'PAID' OR order_status = 'DELIVERED'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
            return 0.0;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public int countTotalOrders() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order(
                rs.getInt("order_id"),
                rs.getInt("user_id"),
                (Integer) rs.getObject("address_id"),
                rs.getDouble("total_amount"),
                rs.getDouble("discount"),
                rs.getString("payment_method"),
                rs.getString("payment_status"),
                rs.getString("order_status"),
                rs.getTimestamp("created_at")
        );
        o.setUserName(rs.getString("user_name"));
        o.setUserEmail(rs.getString("user_email"));

        if (rs.getObject("address_id") != null) {
            Address a = new Address();
            a.setAddressId(rs.getInt("address_id"));
            a.setAddressLine(rs.getString("address_line"));
            a.setCity(rs.getString("city"));
            a.setState(rs.getString("state"));
            a.setPincode(rs.getString("pincode"));
            a.setAddressType(rs.getString("address_type"));
            o.setAddress(a);
        }
        return o;
    }
}
