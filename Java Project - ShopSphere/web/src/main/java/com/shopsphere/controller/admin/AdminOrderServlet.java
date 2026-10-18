package com.shopsphere.controller.admin;

import com.shopsphere.dao.OrderDAO;
import com.shopsphere.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller managing customer order fulfillment and status transitions.
 */
@WebServlet(name = "AdminOrderServlet", urlPatterns = {"/admin/orders"})
public class AdminOrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Order> orders = orderDAO.findAll();
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/admin/orders.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error loading orders: " + e.getMessage());
            request.getRequestDispatcher("/admin/orders.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String status = request.getParameter("orderStatus");

        try {
            orderDAO.updateOrderStatus(orderId, status);
            session.setAttribute("flashSuccess", "Order #" + orderId + " status updated to " + status);
        } catch (SQLException e) {
            session.setAttribute("flashError", "Failed to update order status: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}
