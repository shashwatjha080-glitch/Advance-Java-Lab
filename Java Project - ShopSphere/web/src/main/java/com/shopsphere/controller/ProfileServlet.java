package com.shopsphere.controller;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.Address;
import com.shopsphere.model.User;
import com.shopsphere.util.PasswordUtil;
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
 * Controller managing customer profile information, password reset, and saved addresses.
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final AddressDAO addressDAO = new AddressDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");

        try {
            User user = userDAO.findById(sessionUser.getUserId());
            List<Address> addresses = addressDAO.findByUserId(user.getUserId());

            request.setAttribute("userProfile", user);
            request.setAttribute("addresses", addresses);
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error loading profile: " + e.getMessage());
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");

        try {
            if ("update_info".equalsIgnoreCase(action)) {
                String name = request.getParameter("name");
                String mobile = request.getParameter("mobile");
                user.setName(name);
                user.setMobile(mobile);
                userDAO.updateProfile(user);
                session.setAttribute("user", user);
                session.setAttribute("flashSuccess", "Profile details updated successfully.");
            } else if ("change_password".equalsIgnoreCase(action)) {
                String oldPass = request.getParameter("oldPassword");
                String newPass = request.getParameter("newPassword");
                String confirmPass = request.getParameter("confirmPassword");

                User current = userDAO.findById(user.getUserId());
                if (!PasswordUtil.verifyPassword(oldPass, current.getPassword())) {
                    session.setAttribute("flashError", "Incorrect current password");
                } else if (!newPass.equals(confirmPass)) {
                    session.setAttribute("flashError", "New passwords do not match");
                } else if (newPass.length() < 6) {
                    session.setAttribute("flashError", "Password must be at least 6 characters");
                } else {
                    userDAO.updatePassword(user.getUserId(), PasswordUtil.hashPassword(newPass));
                    session.setAttribute("flashSuccess", "Password changed successfully.");
                }
            } else if ("add_address".equalsIgnoreCase(action)) {
                Address address = new Address();
                address.setUserId(user.getUserId());
                address.setAddressLine(request.getParameter("addressLine"));
                address.setCity(request.getParameter("city"));
                address.setState(request.getParameter("state"));
                address.setPincode(request.getParameter("pincode"));
                address.setAddressType(request.getParameter("addressType"));
                addressDAO.addAddress(address);
                session.setAttribute("flashSuccess", "Address added successfully.");
            } else if ("delete_address".equalsIgnoreCase(action)) {
                int addressId = Integer.parseInt(request.getParameter("addressId"));
                addressDAO.delete(addressId, user.getUserId());
                session.setAttribute("flashSuccess", "Address deleted.");
            }
        } catch (Exception e) {
            session.setAttribute("flashError", "Profile operation failed: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/profile");
    }
}
