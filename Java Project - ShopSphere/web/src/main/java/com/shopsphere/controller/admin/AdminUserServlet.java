package com.shopsphere.controller.admin;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
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
 * Controller managing customer accounts and account deactivation/activation.
 */
@WebServlet(name = "AdminUserServlet", urlPatterns = {"/admin/users"})
public class AdminUserServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<User> users = userDAO.findAll();
            request.setAttribute("users", users);
            request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error loading customer list: " + e.getMessage());
            request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        int userId = Integer.parseInt(request.getParameter("userId"));
        String status = request.getParameter("status");

        try {
            userDAO.updateStatus(userId, status);
            session.setAttribute("flashSuccess", "User account status updated to " + status);
        } catch (SQLException e) {
            session.setAttribute("flashError", "Failed to update user status: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}
