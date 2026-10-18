package com.shopsphere.controller;

import com.shopsphere.dao.WishlistDAO;
import com.shopsphere.model.User;
import com.shopsphere.model.WishlistItem;
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
 * Controller managing customer wishlist operations.
 */
@WebServlet(name = "WishlistServlet", urlPatterns = {"/wishlist"})
public class WishlistServlet extends HttpServlet {

    private final WishlistDAO wishlistDAO = new WishlistDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        try {
            List<WishlistItem> wishlistItems = wishlistDAO.getWishlistItems(user.getUserId());
            request.setAttribute("wishlistItems", wishlistItems);
            request.getRequestDispatcher("/wishlist.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error loading wishlist: " + e.getMessage());
            request.getRequestDispatcher("/wishlist.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        int productId = Integer.parseInt(request.getParameter("productId"));

        try {
            if ("add".equalsIgnoreCase(action)) {
                wishlistDAO.addToWishlist(user.getUserId(), productId);
                session.setAttribute("flashSuccess", "Product added to your wishlist!");
            } else if ("remove".equalsIgnoreCase(action)) {
                wishlistDAO.removeFromWishlist(user.getUserId(), productId);
                session.setAttribute("flashSuccess", "Product removed from your wishlist.");
            }
        } catch (SQLException e) {
            session.setAttribute("flashError", "Failed to update wishlist: " + e.getMessage());
        }

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/wishlist");
        }
    }
}
