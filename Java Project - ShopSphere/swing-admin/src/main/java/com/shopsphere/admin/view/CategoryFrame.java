package com.shopsphere.admin.view;

import com.shopsphere.admin.dao.AdminDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

/**
 * Swing Category Management Window.
 */
public class CategoryFrame extends JFrame {

    private final AdminDAO adminDAO = new AdminDAO();
    private JTable categoryTable;
    private DefaultTableModel tableModel;

    private JTextField idField;
    private JTextField nameField;
    private JTextArea descArea;
    private JCheckBox statusCheck;

    public CategoryFrame() {
        super("Category Management - ShopSphere");
        setSize(700, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Vector<String> cols = new Vector<>();
        cols.add("ID");
        cols.add("Category Name");
        cols.add("Description");
        cols.add("Status");

        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        categoryTable = new JTable(tableModel);
        categoryTable.setRowHeight(24);

        categoryTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = categoryTable.getSelectedRow();
                if (r >= 0) {
                    idField.setText(tableModel.getValueAt(r, 0).toString());
                    nameField.setText(tableModel.getValueAt(r, 1).toString());
                    descArea.setText(tableModel.getValueAt(r, 2) != null ? tableModel.getValueAt(r, 2).toString() : "");
                    statusCheck.setSelected("Active".equalsIgnoreCase(tableModel.getValueAt(r, 3).toString()));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(categoryTable);
        scroll.setBorder(BorderFactory.createTitledBorder("Existing Categories"));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Category Form"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        idField = new JTextField(6);
        idField.setEditable(false);
        nameField = new JTextField(15);
        descArea = new JTextArea(3, 20);
        statusCheck = new JCheckBox("Active", true);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Category ID:"), gbc);
        gbc.gridx = 1; formPanel.add(idField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; formPanel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; formPanel.add(new JScrollPane(descArea), gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; formPanel.add(statusCheck, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> saveCategory(false));
        JButton updateBtn = new JButton("Update");
        updateBtn.addActionListener(e -> saveCategory(true));
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setForeground(Color.RED);
        deleteBtn.addActionListener(e -> deleteCategory());
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadCategories());

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        JPanel south = new JPanel(new BorderLayout());
        south.add(formPanel, BorderLayout.CENTER);
        south.add(btnPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        loadCategories();
    }

    private void loadCategories() {
        try {
            tableModel.setRowCount(0);
            Vector<Vector<Object>> data = adminDAO.getCategoriesTableData();
            for (Vector<Object> r : data) {
                tableModel.addRow(r);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void saveCategory(boolean isUpdate) {
        try {
            Integer id = isUpdate && !idField.getText().trim().isEmpty() ? Integer.parseInt(idField.getText().trim()) : null;
            String name = nameField.getText().trim();
            String desc = descArea.getText().trim();
            boolean status = statusCheck.isSelected();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Category Name is required.");
                return;
            }

            adminDAO.saveCategory(id, name, desc, status);
            loadCategories();
            idField.setText("");
            nameField.setText("");
            descArea.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void deleteCategory() {
        if (idField.getText().trim().isEmpty()) return;
        try {
            int id = Integer.parseInt(idField.getText().trim());
            adminDAO.deleteCategory(id);
            loadCategories();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
