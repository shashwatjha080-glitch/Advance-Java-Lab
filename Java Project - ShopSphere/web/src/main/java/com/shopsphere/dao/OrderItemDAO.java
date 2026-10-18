package com.shopsphere.dao;

import com.shopsphere.model.OrderItem;
import com.shopsphere.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for individual order items.
 */
public class OrderItemDAO {

    public List<OrderItem> findByOrderId(int orderId) throws SQLException {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT oi.*, p.name AS product_name, p.brand AS product_brand, p.image_url " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.product_id " +
                     "WHERE oi.order_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem item = new OrderItem(
                        rs.getInt("order_item_id"),
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );
                item.setProductName(rs.getString("product_name"));
                item.setProductBrand(rs.getString("product_brand"));
                item.setProductImageUrl(rs.getString("image_url"));
                list.add(item);
            }
            return list;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public List<OrderItem> findByOrderId(int orderId, Connection conn) throws SQLException {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT oi.*, p.name AS product_name, p.brand AS product_brand, p.image_url " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.product_id " +
                     "WHERE oi.order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem(
                            rs.getInt("order_item_id"),
                            rs.getInt("order_id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            rs.getDouble("price")
                    );
                    item.setProductName(rs.getString("product_name"));
                    item.setProductBrand(rs.getString("product_brand"));
                    item.setProductImageUrl(rs.getString("image_url"));
                    list.add(item);
                }
            }
        }
        return list;
    }
}
