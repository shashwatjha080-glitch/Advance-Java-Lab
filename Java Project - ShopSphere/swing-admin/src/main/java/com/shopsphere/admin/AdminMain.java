package com.shopsphere.admin;

import com.shopsphere.admin.view.LoginFrame;

import javax.swing.*;

/**
 * Entry point for the ShopSphere Desktop Swing Administration System.
 * Demonstrates Pluggable Look and Feel (PLAF) and Swing MVC pattern.
 */
public class AdminMain {

    public static void main(String[] args) {
        // Demonstrate Pluggable Look and Feel from RTU Syllabus
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("[AdminMain] System Look & Feel not available, using default Swing appearance: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
