package com.shopsphere.admin.view;

import com.shopsphere.admin.dao.AdminDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

/**
 * Swing Customer Management Window.
 */
public class CustomerFrame extends JFrame {

    private final AdminDAO adminDAO = new AdminDAO();
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public CustomerFrame() {
        super("Customer Accounts - ShopSphere");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Vector<String> cols = new Vector<>();
        cols.add("ID");
        cols.add("Customer Name");
        cols.add("Email");
        cols.add("Mobile");
        cols.add("Account Status");
        cols.add("Joined Date");

        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        customerTable = new JTable(tableModel);
        customerTable.setRowHeight(24);

        JScrollPane scroll = new JScrollPane(customerTable);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton toggleBtn = new JButton("Toggle Active/Inactive");
        toggleBtn.addActionListener(e -> toggleStatus());

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadCustomers());

        btnPanel.add(toggleBtn);
        btnPanel.add(refreshBtn);

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        loadCustomers();
    }

    private void loadCustomers() {
        try {
            tableModel.setRowCount(0);
            Vector<Vector<Object>> data = adminDAO.getCustomersTableData();
            for (Vector<Object> r : data) {
                tableModel.addRow(r);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void toggleStatus() {
        int r = customerTable.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a customer from the table first.");
            return;
        }
        try {
            int userId = Integer.parseInt(tableModel.getValueAt(r, 0).toString());
            String current = tableModel.getValueAt(r, 4).toString();
            String newStatus = "ACTIVE".equalsIgnoreCase(current) ? "INACTIVE" : "ACTIVE";
            adminDAO.updateUserStatus(userId, newStatus);
            loadCustomers();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
