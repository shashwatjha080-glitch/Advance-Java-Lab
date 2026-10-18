package com.shopsphere.dao;

import com.shopsphere.model.Address;
import com.shopsphere.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for customer shipping and billing addresses.
 */
public class AddressDAO {

    public boolean addAddress(Address address) throws SQLException {
        String sql = "INSERT INTO addresses (user_id, address_line, city, state, pincode, address_type) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, address.getUserId());
            ps.setString(2, address.getAddressLine());
            ps.setString(3, address.getCity());
            ps.setString(4, address.getState());
            ps.setString(5, address.getPincode());
            ps.setString(6, address.getAddressType() != null ? address.getAddressType() : "HOME");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    address.setAddressId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public List<Address> findByUserId(int userId) throws SQLException {
        List<Address> list = new ArrayList<>();
        String sql = "SELECT * FROM addresses WHERE user_id = ? ORDER BY address_id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAddress(rs));
            }
            return list;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public Address findById(int addressId) throws SQLException {
        String sql = "SELECT * FROM addresses WHERE address_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, addressId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapAddress(rs);
            }
            return null;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public boolean delete(int addressId, int userId) throws SQLException {
        String sql = "DELETE FROM addresses WHERE address_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, addressId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    private Address mapAddress(ResultSet rs) throws SQLException {
        return new Address(
                rs.getInt("address_id"),
                rs.getInt("user_id"),
                rs.getString("address_line"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("pincode"),
                rs.getString("address_type"),
                rs.getTimestamp("created_at")
        );
    }
}
