package com.shopsphere.admin.view;

import com.shopsphere.admin.dao.AdminDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

/**
 * Swing Inventory & Stock Replenishment window.
 */
public class InventoryFrame extends JFrame {

    private final AdminDAO adminDAO = new AdminDAO();
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    private JTextField thresholdField;
    private JTextField restockQtyField;

    public InventoryFrame() {
        super("Inventory & Low Stock Monitoring - ShopSphere");
        setSize(750, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Vector<String> cols = new Vector<>();
        cols.add("Product ID");
        cols.add("Product Name");
        cols.add("Brand");
        cols.add("Current Stock");

        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        inventoryTable = new JTable(tableModel);
        inventoryTable.setRowHeight(24);

        JScrollPane scroll = new JScrollPane(inventoryTable);

        // Top Filter Bar
        JPanel topFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topFilter.add(new JLabel("Threshold (units <=):"));
        thresholdField = new JTextField("10", 4);
        topFilter.add(thresholdField);
        JButton filterBtn = new JButton("Filter Alerts");
        filterBtn.addActionListener(e -> loadLowStock());
        topFilter.add(filterBtn);

        // Bottom Restock Bar
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomBar.add(new JLabel("Add Stock to Selected:"));
        restockQtyField = new JTextField("25", 5);
        bottomBar.add(restockQtyField);
        JButton restockBtn = new JButton("Restock Product");
        restockBtn.addActionListener(e -> restock());
        bottomBar.add(restockBtn);

        setLayout(new BorderLayout());
        add(topFilter, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(bottomBar, BorderLayout.SOUTH);

        loadLowStock();
    }

    private void loadLowStock() {
        try {
            int threshold = Integer.parseInt(thresholdField.getText().trim());
            tableModel.setRowCount(0);
            Vector<Vector<Object>> data = adminDAO.getInventoryTableData(threshold);
            for (Vector<Object> r : data) {
                tableModel.addRow(r);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void restock() {
        int r = inventoryTable.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a product to restock.");
            return;
        }
        try {
            int pId = Integer.parseInt(tableModel.getValueAt(r, 0).toString());
            int current = Integer.parseInt(tableModel.getValueAt(r, 3).toString());
            int addQty = Integer.parseInt(restockQtyField.getText().trim());

            adminDAO.updateStock(pId, current + addQty);
            JOptionPane.showMessageDialog(this, "Product #" + pId + " restocked to " + (current + addQty) + " units.");
            loadLowStock();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
