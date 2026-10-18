package com.shopsphere.controller;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.model.User;
import com.shopsphere.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller handling customer authentication and session initialization.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final AuthenticationService authService = new AuthenticationService();
    private final CartDAO cartDAO = new CartDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        try {
            User user = authService.login(email, password);
            if (user != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("role", user.getRole());

                // Update cart count badge
                int cartCount = cartDAO.getCartItemCount(user.getUserId());
                session.setAttribute("cartItemCount", cartCount);

                // Remember user email in cookie (safe convenience feature)
                if ("on".equalsIgnoreCase(rememberMe) || "true".equalsIgnoreCase(rememberMe)) {
                    Cookie emailCookie = new Cookie("savedEmail", user.getEmail());
                    emailCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
                    emailCookie.setPath(request.getContextPath() + "/");
                    response.addCookie(emailCookie);
                }

                // Redirect to target or home
                String target = (String) session.getAttribute("redirectAfterLogin");
                session.removeAttribute("redirectAfterLogin");
                session.removeAttribute("authError");

                if (target != null && !target.isEmpty()) {
                    response.sendRedirect(target);
                } else if (user.isAdmin()) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                }
            } else {
                request.setAttribute("errorMessage", "Invalid email address or password. Please try again.");
                request.setAttribute("enteredEmail", email);
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("enteredEmail", email);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
