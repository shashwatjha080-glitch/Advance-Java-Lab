package com.shopsphere.admin.view;

import com.shopsphere.admin.dao.AdminDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Swing Admin Login Window using standard Java Swing components.
 */
public class LoginFrame extends JFrame {

    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton cancelButton;
    private final AdminDAO adminDAO = new AdminDAO();

    public LoginFrame() {
        super("ShopSphere - Desktop Administrator Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(460, 340);
        setLocationRelativeTo(null);
        setResizable(false);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(15, 23, 42)); // Dark Navy
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
        JLabel titleLabel = new JLabel("ShopSphere Admin Portal");
        titleLabel.setForeground(new Color(250, 204, 21)); // Gold
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email row
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel emailLabel = new JLabel("Admin Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        emailField = new JTextField("admin@shopsphere.com", 18);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(emailField, gbc);

        // Password row
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        passwordField = new JPasswordField("admin123", 18);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(passwordField, gbc);

        // Demo hint label
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JLabel hintLabel = new JLabel("Demo: admin@shopsphere.com / admin123", SwingConstants.CENTER);
        hintLabel.setForeground(Color.GRAY);
        hintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        formPanel.add(hintLabel, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(79, 70, 229)); // Indigo
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(this::handleLogin);

        cancelButton = new JButton("Exit");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        // Frame Layout
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Root pane default button (Enter key logs in)
        getRootPane().setDefaultButton(loginButton);
    }

    private void handleLogin(ActionEvent e) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both admin email and password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean authenticated = adminDAO.authenticateAdmin(email, password);
            if (authenticated) {
                JOptionPane.showMessageDialog(this, "Authentication successful! Welcome to ShopSphere Admin.", "Login Success", JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.invokeLater(() -> {
                    new AdminDashboard().setVisible(true);
                    this.dispose();
                });
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials or unauthorized account.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database connection error:\n" + ex.getMessage(), "Connection Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
