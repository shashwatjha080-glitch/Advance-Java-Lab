package com.shopsphere.dao;

import com.shopsphere.model.Category;
import com.shopsphere.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Product Categories.
 */
public class CategoryDAO {

    public List<Category> findAll(boolean onlyActive) throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = onlyActive ? "SELECT * FROM categories WHERE status = TRUE ORDER BY name ASC"
                                : "SELECT * FROM categories ORDER BY category_id ASC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapCategory(rs));
            }
            return list;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public Category findById(int categoryId) throws SQLException {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapCategory(rs);
            }
            return null;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public boolean create(Category category) throws SQLException {
        String sql = "INSERT INTO categories (name, description, status) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setBoolean(3, category.isStatus());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    category.setCategoryId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public boolean update(Category category) throws SQLException {
        String sql = "UPDATE categories SET name = ?, description = ?, status = ? WHERE category_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setBoolean(3, category.isStatus());
            ps.setInt(4, category.getCategoryId());
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    public boolean delete(int categoryId) throws SQLException {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId);
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    private Category mapCategory(ResultSet rs) throws SQLException {
        return new Category(
                rs.getInt("category_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBoolean("status")
        );
    }
}
