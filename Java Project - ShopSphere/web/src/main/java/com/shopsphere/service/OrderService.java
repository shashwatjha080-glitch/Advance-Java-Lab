package com.shopsphere.service;

import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.Order;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service managing customer orders, tracking, status progression, and admin dashboard statistics.
 */
public class OrderService {

    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;
    private final UserDAO userDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.productDAO = new ProductDAO();
        this.userDAO = new UserDAO();
    }

    public OrderService(OrderDAO orderDAO, ProductDAO productDAO, UserDAO userDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
        this.userDAO = userDAO;
    }

    public Order getOrderDetails(int orderId, int userId, boolean isAdmin) throws SQLException {
        Order order = orderDAO.findById(orderId);
        if (order == null) return null;
        if (!isAdmin && order.getUserId() != userId) {
            throw new SecurityException("Unauthorized access to order details");
        }
        return order;
    }

    public List<Order> getUserOrders(int userId) throws SQLException {
        return orderDAO.findByUserId(userId);
    }

    public List<Order> getAllOrders() throws SQLException {
        return orderDAO.findAll();
    }

    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        return orderDAO.updateOrderStatus(orderId, status);
    }

    public Map<String, Object> getDashboardStats() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRevenue", orderDAO.getTotalRevenue());
        stats.put("totalOrders", orderDAO.countTotalOrders());
        stats.put("totalCustomers", userDAO.findAll().size());
        stats.put("lowStockCount", productDAO.findLowStock(10).size());
        return stats;
    }
}
