package com.shopsphere.dao;

import com.shopsphere.model.Product;
import com.shopsphere.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Products and Inventory.
 */
public class ProductDAO {

    public List<Product> findAll(Integer categoryId, String searchQuery, String sortBy, int offset, int limit) throws SQLException {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, c.name AS category_name FROM products p " +
                "LEFT JOIN categories c ON p.category_id = c.category_id WHERE p.status = TRUE "
        );

        List<Object> params = new ArrayList<>();
        if (categoryId != null && categoryId > 0) {
            sql.append("AND p.category_id = ? ");
            params.add(categoryId);
        }
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            sql.append("AND (p.name LIKE ? OR p.brand LIKE ? OR p.description LIKE ?) ");
            String wild = "%" + searchQuery.trim() + "%";
            params.add(wild);
            params.add(wild);
            params.add(wild);
        }

        // Sorting
        if ("price_asc".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.price ASC ");
        } else if ("price_desc".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.price DESC ");
        } else if ("discount".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.discount DESC ");
        } else if ("name_asc".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.name ASC ");
        } else {
            sql.append("ORDER BY p.product_id DESC ");
        }

        sql.append("LIMIT ? OFFSET ?");
        params.add(limit > 0 ? limit : 20);
        params.add(offset >= 0 ? offset : 0);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapProduct(rs));
            }
            return list;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public int countAll(Integer categoryId, String searchQuery) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM products p WHERE p.status = TRUE ");
        List<Object> params = new ArrayList<>();
        if (categoryId != null && categoryId > 0) {
            sql.append("AND p.category_id = ? ");
            params.add(categoryId);
        }
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            sql.append("AND (p.name LIKE ? OR p.brand LIKE ? OR p.description LIKE ?) ");
            String wild = "%" + searchQuery.trim() + "%";
            params.add(wild);
            params.add(wild);
            params.add(wild);
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public List<Product> findFeatured(int limit) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.status = TRUE ORDER BY p.discount DESC, p.product_id DESC LIMIT ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapProduct(rs));
            }
            return list;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public Product findById(int productId) throws SQLException {
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id WHERE p.product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapProduct(rs);
            }
            return null;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public List<Product> findAllForAdmin() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id ORDER BY p.product_id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapProduct(rs));
            }
            return list;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public List<Product> findLowStock(int threshold) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.stock <= ? ORDER BY p.stock ASC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, threshold);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapProduct(rs));
            }
            return list;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public boolean create(Product product) throws SQLException {
        String sql = "INSERT INTO products (category_id, name, brand, description, price, discount, stock, image_url, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getBrand());
            ps.setString(4, product.getDescription());
            ps.setDouble(5, product.getPrice());
            ps.setDouble(6, product.getDiscount());
            ps.setInt(7, product.getStock());
            ps.setString(8, product.getImageUrl());
            ps.setBoolean(9, product.isStatus());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    product.setProductId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public boolean update(Product product) throws SQLException {
        String sql = "UPDATE products SET category_id = ?, name = ?, brand = ?, description = ?, " +
                     "price = ?, discount = ?, stock = ?, image_url = ?, status = ? WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getBrand());
            ps.setString(4, product.getDescription());
            ps.setDouble(5, product.getPrice());
            ps.setDouble(6, product.getDiscount());
            ps.setInt(7, product.getStock());
            ps.setString(8, product.getImageUrl());
            ps.setBoolean(9, product.isStatus());
            ps.setInt(10, product.getProductId());
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    public boolean setStock(int productId, int newStock) throws SQLException {
        String sql = "UPDATE products SET stock = ? WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    public boolean updateStock(int productId, int quantityDelta, Connection conn) throws SQLException {
        String sql = "UPDATE products SET stock = stock + ? WHERE product_id = ? AND (stock + ?) >= 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantityDelta);
            ps.setInt(2, productId);
            ps.setInt(3, quantityDelta);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int productId) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product p = new Product(
                rs.getInt("product_id"),
                rs.getInt("category_id"),
                rs.getString("name"),
                rs.getString("brand"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getDouble("discount"),
                rs.getInt("stock"),
                rs.getString("image_url"),
                rs.getBoolean("status"),
                rs.getTimestamp("created_at")
        );
        try {
            p.setCategoryName(rs.getString("category_name"));
        } catch (SQLException ignored) {}
        return p;
    }
}
