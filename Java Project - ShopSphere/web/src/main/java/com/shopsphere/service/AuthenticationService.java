package com.shopsphere.service;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
import com.shopsphere.util.PasswordUtil;
import com.shopsphere.util.ValidationUtil;

import java.sql.SQLException;

/**
 * Service orchestrating user authentication, registration and credential verification.
 */
public class AuthenticationService {

    private final UserDAO userDAO;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }

    public AuthenticationService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Authenticates user against stored SHA-256 hashed password.
     */
    public User login(String email, String password) throws SQLException, IllegalArgumentException {
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        User user = userDAO.findByEmail(email);
        if (user == null) {
            return null; // User not found
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new IllegalStateException("Your account has been deactivated. Please contact support.");
        }

        if (PasswordUtil.verifyPassword(password, user.getPassword())) {
            return user;
        }

        return null; // Invalid password
    }

    /**
     * Registers a new customer after rigorous validation and email uniqueness checks.
     */
    public User register(String name, String email, String password, String mobile) throws SQLException, IllegalArgumentException {
        if (!ValidationUtil.isNotEmpty(name)) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address format");
        }
        if (!ValidationUtil.isValidPassword(password)) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        if (!ValidationUtil.isValidMobile(mobile)) {
            throw new IllegalArgumentException("Mobile number must be 10 to 15 digits");
        }

        if (userDAO.findByEmail(email) != null) {
            throw new IllegalArgumentException("An account with this email address already exists");
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        User user = new User();
        user.setName(name.trim());
        user.setEmail(email.toLowerCase().trim());
        user.setPassword(hashedPassword);
        user.setMobile(mobile != null ? mobile.trim() : "");
        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");

        boolean success = userDAO.register(user);
        if (success) {
            return user;
        }
        throw new SQLException("Failed to create user account");
    }

    public boolean isEmailRegistered(String email) throws SQLException {
        return userDAO.findByEmail(email) != null;
    }
}
