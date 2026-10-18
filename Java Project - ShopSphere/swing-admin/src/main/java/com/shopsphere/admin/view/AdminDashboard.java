package com.shopsphere.admin.view;

import com.shopsphere.admin.dao.AdminDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

/**
 * Main Swing Administration Control Center with live KPI cards and navigation.
 */
public class AdminDashboard extends JFrame {

    private final AdminDAO adminDAO = new AdminDAO();
    private JLabel revLabel;
    private JLabel ordersLabel;
    private JLabel custLabel;
    private JLabel stockLabel;
    private JTable ordersTable;
    private DefaultTableModel ordersTableModel;

    public AdminDashboard() {
        super("ShopSphere Enterprise - Desktop Administration Suite");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 640);
        setLocationRelativeTo(null);

        // Header Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(15, 23, 42));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("ShopSphere Enterprise Dashboard");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });
        topPanel.add(logoutBtn, BorderLayout.EAST);

        // Navigation Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(241, 245, 249));
        toolBar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JButton prodBtn = createNavButton("Products & Inventory", "bi-box-seam");
        prodBtn.addActionListener(e -> new ProductFrame().setVisible(true));

        JButton catBtn = createNavButton("Categories", "bi-tags");
        catBtn.addActionListener(e -> new CategoryFrame().setVisible(true));

        JButton orderBtn = createNavButton("Orders", "bi-receipt");
        orderBtn.addActionListener(e -> new OrderFrame().setVisible(true));

        JButton custBtn = createNavButton("Customers", "bi-people");
        custBtn.addActionListener(e -> new CustomerFrame().setVisible(true));

        JButton invBtn = createNavButton("Low Stock Alerts", "bi-bell");
        invBtn.addActionListener(e -> new InventoryFrame().setVisible(true));

        JButton refreshBtn = createNavButton("Refresh Data", "bi-arrow-clockwise");
        refreshBtn.addActionListener(e -> loadDashboardData());

        toolBar.add(prodBtn);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(catBtn);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(orderBtn);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(custBtn);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(invBtn);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(refreshBtn);

        // KPI Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20));
        statsPanel.setBackground(new Color(248, 250, 252));

        revLabel = new JLabel("₹0.00", SwingConstants.CENTER);
        ordersLabel = new JLabel("0", SwingConstants.CENTER);
        custLabel = new JLabel("0", SwingConstants.CENTER);
        stockLabel = new JLabel("0", SwingConstants.CENTER);

        statsPanel.add(createCard("Total Revenue", revLabel, new Color(79, 70, 229)));
        statsPanel.add(createCard("Total Orders", ordersLabel, new Color(16, 185, 129)));
        statsPanel.add(createCard("Customers", custLabel, new Color(14, 165, 233)));
        statsPanel.add(createCard("Low Stock Items", stockLabel, new Color(239, 68, 68)));

        // Recent Orders Table Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel tableTitle = new JLabel("Recent Orders Overview");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        centerPanel.add(tableTitle, BorderLayout.NORTH);

        Vector<String> columns = new Vector<>();
        columns.add("Order ID");
        columns.add("Customer");
        columns.add("Amount (₹)");
        columns.add("Payment Method");
        columns.add("Payment Status");
        columns.add("Order Status");
        columns.add("Order Date");

        ordersTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        ordersTable = new JTable(ordersTableModel);
        ordersTable.setRowHeight(26);
        ordersTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ordersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Layout Assembly
        JPanel northContainer = new JPanel(new BorderLayout());
        northContainer.add(topPanel, BorderLayout.NORTH);
        northContainer.add(toolBar, BorderLayout.CENTER);
        northContainer.add(statsPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(northContainer, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        loadDashboardData();
    }

    private JButton createNavButton(String text, String iconName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        return btn;
    }

    private JPanel createCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLbl = new JLabel(title.toUpperCase());
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleLbl.setForeground(new Color(100, 116, 139));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(new Color(15, 23, 42));

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private void loadDashboardData() {
        try {
            Map<String, Object> stats = adminDAO.getDashboardStats();
            double rev = (Double) stats.get("revenue");
            revLabel.setText("₹" + NumberFormat.getNumberInstance(Locale.US).format(rev));
            ordersLabel.setText(String.valueOf(stats.get("orders")));
            custLabel.setText(String.valueOf(stats.get("customers")));
            stockLabel.setText(String.valueOf(stats.get("lowStock")));

            // Refresh recent orders
            ordersTableModel.setRowCount(0);
            Vector<Vector<Object>> data = adminDAO.getOrdersTableData();
            for (Vector<Object> row : data) {
                ordersTableModel.addRow(row);
            }
        } catch (Exception ex) {
            System.err.println("[AdminDashboard] Error loading dashboard: " + ex.getMessage());
        }
    }
}
