package com.shopsphere.admin.view;

import com.shopsphere.admin.dao.AdminDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

/**
 * Swing Order Management and Status Progression Window.
 */
public class OrderFrame extends JFrame {

    private final AdminDAO adminDAO = new AdminDAO();
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusCombo;

    public OrderFrame() {
        super("Order Processing & Logistics - ShopSphere");
        setSize(850, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Vector<String> cols = new Vector<>();
        cols.add("Order ID");
        cols.add("Customer");
        cols.add("Total (₹)");
        cols.add("Payment");
        cols.add("Pay Status");
        cols.add("Order Status");
        cols.add("Created At");

        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        orderTable = new JTable(tableModel);
        orderTable.setRowHeight(24);

        JScrollPane scroll = new JScrollPane(orderTable);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        controlPanel.add(new JLabel("Update Selected Order Status to:"));

        statusCombo = new JComboBox<>(new String[]{"PLACED", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"});
        controlPanel.add(statusCombo);

        JButton updateBtn = new JButton("Save Status");
        updateBtn.addActionListener(e -> updateStatus());
        controlPanel.add(updateBtn);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadOrders());
        controlPanel.add(refreshBtn);

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        loadOrders();
    }

    private void loadOrders() {
        try {
            tableModel.setRowCount(0);
            Vector<Vector<Object>> data = adminDAO.getOrdersTableData();
            for (Vector<Object> r : data) {
                tableModel.addRow(r);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void updateStatus() {
        int r = orderTable.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select an order from the table first.");
            return;
        }
        try {
            int orderId = Integer.parseInt(tableModel.getValueAt(r, 0).toString());
            String newStatus = (String) statusCombo.getSelectedItem();
            adminDAO.updateOrderStatus(orderId, newStatus);
            JOptionPane.showMessageDialog(this, "Order #" + orderId + " updated to " + newStatus);
            loadOrders();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
