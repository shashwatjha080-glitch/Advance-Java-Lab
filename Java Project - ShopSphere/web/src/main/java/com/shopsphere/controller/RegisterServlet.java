package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller handling customer registration and automatic login.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private final AuthenticationService authService = new AuthenticationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String mobile = request.getParameter("mobile");

        if (password == null || !password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match");
            preserveFields(request, name, email, mobile);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        try {
            User newUser = authService.register(name, email, password, mobile);
            HttpSession session = request.getSession(true);
            session.setAttribute("user", newUser);
            session.setAttribute("role", newUser.getRole());
            session.setAttribute("cartItemCount", 0);
            session.setAttribute("flashSuccess", "Registration successful! Welcome to ShopSphere, " + newUser.getName() + ".");

            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            preserveFields(request, name, email, mobile);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (Exception ex) {
            request.setAttribute("errorMessage", "An error occurred during registration. Please try again.");
            preserveFields(request, name, email, mobile);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    private void preserveFields(HttpServletRequest request, String name, String email, String mobile) {
        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.setAttribute("mobile", mobile);
    }
}
