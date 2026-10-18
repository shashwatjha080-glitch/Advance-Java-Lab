package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Represents a customer shipping or billing address.
 */
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    private int addressId;
    private int userId;
    private String addressLine;
    private String city;
    private String state;
    private String pincode;
    private String addressType; // 'HOME', 'WORK', 'OTHER'
    private Timestamp createdAt;

    public Address() {}

    public Address(int addressId, int userId, String addressLine, String city, String state, String pincode, String addressType, Timestamp createdAt) {
        this.addressId = addressId;
        this.userId = userId;
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.addressType = addressType;
        this.createdAt = createdAt;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormattedAddress() {
        return addressLine + ", " + city + ", " + state + " - " + pincode + " (" + addressType + ")";
    }

    @Override
    public String toString() {
        return getFormattedAddress();
    }
}
