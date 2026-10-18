package com.shopsphere.controller.admin;

import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.Order;
import com.shopsphere.model.Product;
import com.shopsphere.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Controller rendering Administrator KPI Dashboard, revenue metrics, and low-stock alerts.
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final ProductDAO productDAO = new ProductDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Map<String, Object> stats = orderService.getDashboardStats();
            List<Order> recentOrders = orderDAO.findAll();
            if (recentOrders.size() > 6) {
                recentOrders = recentOrders.subList(0, 6);
            }
            List<Product> lowStock = productDAO.findLowStock(10);

            request.setAttribute("stats", stats);
            request.setAttribute("recentOrders", recentOrders);
            request.setAttribute("lowStockProducts", lowStock);

            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error loading admin dashboard: " + e.getMessage());
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        }
    }
}
