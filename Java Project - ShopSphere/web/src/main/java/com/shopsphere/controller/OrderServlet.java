package com.shopsphere.controller;

import com.shopsphere.model.Order;
import com.shopsphere.model.User;
import com.shopsphere.service.OrderService;
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
 * Controller managing customer order history, order status tracking, and order receipt pages.
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/orders", "/order-details", "/order-success"})
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        try {
            if ("/order-success".equals(path)) {
                String orderIdParam = request.getParameter("orderId");
                if (orderIdParam != null) {
                    int orderId = Integer.parseInt(orderIdParam);
                    Order order = orderService.getOrderDetails(orderId, user.getUserId(), user.isAdmin());
                    request.setAttribute("order", order);
                }
                request.getRequestDispatcher("/order-success.jsp").forward(request, response);
            } else if ("/order-details".equals(path)) {
                String orderIdParam = request.getParameter("id");
                if (orderIdParam == null) {
                    response.sendRedirect(request.getContextPath() + "/orders");
                    return;
                }
                int orderId = Integer.parseInt(orderIdParam);
                Order order = orderService.getOrderDetails(orderId, user.getUserId(), user.isAdmin());
                if (order == null) {
                    session.setAttribute("flashError", "Order not found");
                    response.sendRedirect(request.getContextPath() + "/orders");
                    return;
                }
                request.setAttribute("order", order);
                request.getRequestDispatcher("/order-details.jsp").forward(request, response);
            } else {
                // /orders
                List<Order> orders = orderService.getUserOrders(user.getUserId());
                request.setAttribute("orders", orders);
                request.getRequestDispatcher("/orders.jsp").forward(request, response);
            }
        } catch (SQLException | NumberFormatException e) {
            request.setAttribute("errorMessage", "Error loading order information: " + e.getMessage());
            request.getRequestDispatcher("/orders.jsp").forward(request, response);
        } catch (SecurityException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }
}
