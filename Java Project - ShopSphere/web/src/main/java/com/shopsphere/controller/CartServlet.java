package com.shopsphere.controller;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.User;
import com.shopsphere.service.CheckoutService;
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
 * Controller managing customer shopping cart operations (Add, Update, Remove, Clear).
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    private final CartDAO cartDAO = new CartDAO();
    private final CheckoutService checkoutService = new CheckoutService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        try {
            List<CartItem> cartItems = cartDAO.getCartItems(user.getUserId());
            String couponCode = request.getParameter("coupon");

            CheckoutService.CheckoutCalculation calc = checkoutService.calculateOrder(cartItems, couponCode, user.getUserId());

            request.setAttribute("cartItems", cartItems);
            request.setAttribute("calc", calc);
            session.setAttribute("cartItemCount", cartDAO.getCartItemCount(user.getUserId()));

            request.getRequestDispatcher("/cart.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error loading shopping cart: " + e.getMessage());
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");

        try {
            if ("add".equalsIgnoreCase(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int quantity = 1;
                try {
                    quantity = Math.max(1, Integer.parseInt(request.getParameter("quantity")));
                } catch (Exception ignored) {}

                cartDAO.addToCart(user.getUserId(), productId, quantity);
                session.setAttribute("flashSuccess", "Item added to your shopping cart!");

                String redirectParam = request.getParameter("redirect");
                if ("cart".equalsIgnoreCase(redirectParam)) {
                    response.sendRedirect(request.getContextPath() + "/cart");
                } else {
                    response.sendRedirect(request.getContextPath() + "/product-details?id=" + productId);
                }
                return;
            } else if ("update".equalsIgnoreCase(action)) {
                int cartId = Integer.parseInt(request.getParameter("cartId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                cartDAO.updateQuantity(cartId, user.getUserId(), quantity);
            } else if ("remove".equalsIgnoreCase(action)) {
                int cartId = Integer.parseInt(request.getParameter("cartId"));
                cartDAO.removeFromCart(cartId, user.getUserId());
                session.setAttribute("flashSuccess", "Item removed from cart.");
            } else if ("clear".equalsIgnoreCase(action)) {
                cartDAO.clearCart(user.getUserId());
                session.setAttribute("flashSuccess", "Shopping cart cleared.");
            }
        } catch (Exception e) {
            session.setAttribute("flashError", "Failed to update cart: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
