package com.shopsphere.admin.view;

import com.shopsphere.admin.dao.AdminDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

/**
 * Swing Product and Inventory Management window.
 */
public class ProductFrame extends JFrame {

    private final AdminDAO adminDAO = new AdminDAO();
    private JTable productTable;
    private DefaultTableModel tableModel;

    private JTextField idField;
    private JTextField nameField;
    private JTextField brandField;
    private JTextField catIdField;
    private JTextField priceField;
    private JTextField discountField;
    private JTextField stockField;
    private JTextField imgUrlField;
    private JTextArea descArea;
    private JCheckBox statusCheck;

    public ProductFrame() {
        super("Product Catalog & Stock Management - ShopSphere");
        setSize(980, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table Setup
        Vector<String> cols = new Vector<>();
        cols.add("ID");
        cols.add("Product Name");
        cols.add("Brand");
        cols.add("Category");
        cols.add("Price (₹)");
        cols.add("Discount (%)");
        cols.add("Stock");
        cols.add("Status");

        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        productTable = new JTable(tableModel);
        productTable.setRowHeight(24);
        productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selected = productTable.getSelectedRow();
                if (selected >= 0) {
                    idField.setText(tableModel.getValueAt(selected, 0).toString());
                    nameField.setText(tableModel.getValueAt(selected, 1).toString());
                    brandField.setText(tableModel.getValueAt(selected, 2).toString());
                    priceField.setText(tableModel.getValueAt(selected, 4).toString());
                    discountField.setText(tableModel.getValueAt(selected, 5).toString());
                    stockField.setText(tableModel.getValueAt(selected, 6).toString());
                    statusCheck.setSelected("Active".equalsIgnoreCase(tableModel.getValueAt(selected, 7).toString()));
                }
            }
        });

        JScrollPane tableScroll = new JScrollPane(productTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Existing Products"));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Product Properties"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        idField = new JTextField(6);
        idField.setEditable(false);
        nameField = new JTextField(15);
        brandField = new JTextField(10);
        catIdField = new JTextField("1", 6);
        priceField = new JTextField("999.00", 8);
        discountField = new JTextField("0.00", 6);
        stockField = new JTextField("20", 6);
        imgUrlField = new JTextField("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600", 20);
        descArea = new JTextArea(3, 20);
        statusCheck = new JCheckBox("Active", true);

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Product ID:"), gbc);
        gbc.gridx = 1; formPanel.add(idField, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Product Name:"), gbc);
        gbc.gridx = 3; formPanel.add(nameField, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Brand:"), gbc);
        gbc.gridx = 1; formPanel.add(brandField, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Category ID:"), gbc);
        gbc.gridx = 3; formPanel.add(catIdField, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Price (₹):"), gbc);
        gbc.gridx = 1; formPanel.add(priceField, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Discount (%):"), gbc);
        gbc.gridx = 3; formPanel.add(discountField, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Stock Quantity:"), gbc);
        gbc.gridx = 1; formPanel.add(stockField, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 3; formPanel.add(statusCheck, gbc);

        // Row 4
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Image URL:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; formPanel.add(imgUrlField, gbc);
        gbc.gridwidth = 1;

        // Row 5
        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; formPanel.add(new JScrollPane(descArea), gbc);
        gbc.gridwidth = 1;

        // Action Buttons Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton addBtn = new JButton("Add Product");
        addBtn.setBackground(new Color(79, 70, 229));
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> saveProduct(false));

        JButton updateBtn = new JButton("Update Selected");
        updateBtn.addActionListener(e -> saveProduct(true));

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setForeground(Color.RED);
        deleteBtn.addActionListener(e -> deleteProduct());

        JButton clearBtn = new JButton("Clear Form");
        clearBtn.addActionListener(e -> clearForm());

        JButton refreshBtn = new JButton("Refresh Table");
        refreshBtn.addActionListener(e -> loadProducts());

        btnPanel.add(clearBtn);
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        // Assembly
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(btnPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(tableScroll, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        loadProducts();
    }

    private void loadProducts() {
        try {
            tableModel.setRowCount(0);
            Vector<Vector<Object>> data = adminDAO.getProductsTableData();
            for (Vector<Object> r : data) {
                tableModel.addRow(r);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveProduct(boolean isUpdate) {
        try {
            Integer id = null;
            if (isUpdate) {
                if (idField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Select a product from the table first.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                id = Integer.parseInt(idField.getText().trim());
            }

            int catId = Integer.parseInt(catIdField.getText().trim());
            String name = nameField.getText().trim();
            String brand = brandField.getText().trim();
            String desc = descArea.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            double discount = Double.parseDouble(discountField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());
            String imgUrl = imgUrlField.getText().trim();
            boolean status = statusCheck.isSelected();

            if (name.isEmpty() || brand.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Brand are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = adminDAO.saveProduct(id, catId, name, brand, desc, price, discount, stock, imgUrl, status);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Product " + (isUpdate ? "updated" : "added") + " successfully!");
                clearForm();
                loadProducts();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct() {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a product to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                adminDAO.deleteProduct(id);
                clearForm();
                loadProducts();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        brandField.setText("");
        catIdField.setText("1");
        priceField.setText("999.00");
        discountField.setText("0.00");
        stockField.setText("20");
        descArea.setText("");
        statusCheck.setSelected(true);
    }
}
