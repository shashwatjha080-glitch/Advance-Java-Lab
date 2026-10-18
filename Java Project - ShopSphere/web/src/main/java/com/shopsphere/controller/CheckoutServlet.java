package com.shopsphere.controller;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.dao.CartDAO;
import com.shopsphere.model.Address;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Order;
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
 * Controller managing secure checkout, address selection, coupon calculation,
 * and order submission.
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private final CartDAO cartDAO = new CartDAO();
    private final AddressDAO addressDAO = new AddressDAO();
    private final CheckoutService checkoutService = new CheckoutService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        try {
            List<CartItem> cartItems = cartDAO.getCartItems(user.getUserId());
            if (cartItems.isEmpty()) {
                session.setAttribute("flashError", "Your shopping cart is empty. Please add items before checking out.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            String couponCode = request.getParameter("coupon");
            CheckoutService.CheckoutCalculation calc = checkoutService.calculateOrder(cartItems, couponCode, user.getUserId());
            List<Address> addresses = addressDAO.findByUserId(user.getUserId());

            request.setAttribute("cartItems", cartItems);
            request.setAttribute("calc", calc);
            request.setAttribute("addresses", addresses);
            request.setAttribute("couponCode", couponCode);

            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error loading checkout: " + e.getMessage());
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String addressIdParam = request.getParameter("addressId");
        String paymentMethod = request.getParameter("paymentMethod");
        String couponCode = request.getParameter("couponCode");

        // Handle adding a new address on the fly
        int addressId = 0;
        if ("new".equalsIgnoreCase(addressIdParam) || addressIdParam == null || addressIdParam.isEmpty()) {
            String addressLine = request.getParameter("addressLine");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String pincode = request.getParameter("pincode");
            String addressType = request.getParameter("addressType");

            Address address = new Address();
            address.setUserId(user.getUserId());
            address.setAddressLine(addressLine);
            address.setCity(city);
            address.setState(state);
            address.setPincode(pincode);
            address.setAddressType(addressType != null ? addressType : "HOME");

            try {
                addressDAO.addAddress(address);
                addressId = address.getAddressId();
            } catch (SQLException e) {
                session.setAttribute("flashError", "Failed to save shipping address: " + e.getMessage());
                response.sendRedirect(request.getContextPath() + "/checkout");
                return;
            }
        } else {
            try {
                addressId = Integer.parseInt(addressIdParam);
            } catch (NumberFormatException ignored) {}
        }

        try {
            Order order = checkoutService.placeOrder(user.getUserId(), addressId, paymentMethod, couponCode);
            session.setAttribute("cartItemCount", 0);
            response.sendRedirect(request.getContextPath() + "/order-success?orderId=" + order.getOrderId());
        } catch (Exception e) {
            session.setAttribute("flashError", "Checkout could not be completed: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }
}
