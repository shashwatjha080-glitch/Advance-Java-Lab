package com.shopsphere.service;

import com.shopsphere.model.Coupon;
import com.shopsphere.model.Product;
import com.shopsphere.util.PasswordUtil;
import com.shopsphere.util.ValidationUtil;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests verifying core business logic, hashing, pricing calculation and validations.
 */
public class BusinessLogicTest {

    @Test
    public void testPasswordHashingAndVerification() {
        String plain = "studentSecure123";
        String hashed = PasswordUtil.hashPassword(plain);

        assertNotNull(hashed);
        assertEquals(64, hashed.length(), "SHA-256 hex string must be exactly 64 characters");
        assertTrue(PasswordUtil.verifyPassword(plain, hashed), "Password verification should pass");
        assertFalse(PasswordUtil.verifyPassword("wrongPassword", hashed), "Invalid password must be rejected");
    }

    @Test
    public void testInputValidations() {
        assertTrue(ValidationUtil.isValidEmail("john.doe@example.com"));
        assertTrue(ValidationUtil.isValidEmail("student_123@rtu.ac.in"));
        assertFalse(ValidationUtil.isValidEmail("invalid-email"));
        assertFalse(ValidationUtil.isValidEmail("user@.com"));

        assertTrue(ValidationUtil.isValidMobile("9876543210"));
        assertFalse(ValidationUtil.isValidMobile("123")); // too short

        assertTrue(ValidationUtil.isValidPassword("secret123"));
        assertFalse(ValidationUtil.isValidPassword("123")); // < 6 chars
    }

    @Test
    public void testProductDiscountCalculation() {
        Product p = new Product();
        p.setPrice(1000.00);
        p.setDiscount(20.00); // 20% discount

        assertEquals(800.00, p.getDiscountedPrice(), 0.001);
        assertEquals(200.00, p.getSavingsAmount(), 0.001);

        p.setDiscount(0.00);
        assertEquals(1000.00, p.getDiscountedPrice(), 0.001);
        assertEquals(0.00, p.getSavingsAmount(), 0.001);
    }

    @Test
    public void testCouponCalculation() {
        Coupon percentageCoupon = new Coupon();
        percentageCoupon.setCode("TEST20");
        percentageCoupon.setDiscountType("PERCENTAGE");
        percentageCoupon.setDiscountValue(20.0);
        percentageCoupon.setMinimumOrder(500.0);
        percentageCoupon.setMaximumDiscount(500.0);
        percentageCoupon.setExpiryDate(Date.valueOf(LocalDate.now().plusDays(30)));
        percentageCoupon.setStatus(true);

        // Order below min order: 0 discount
        assertEquals(0.0, percentageCoupon.calculateDiscount(300.0), 0.001);

        // Order 1000 with 20%: 200 discount
        assertEquals(200.0, percentageCoupon.calculateDiscount(1000.0), 0.001);

        // Order 5000 with 20% (1000) capped at maxDiscount 500:
        assertEquals(500.0, percentageCoupon.calculateDiscount(5000.0), 0.001);

        // Test Flat coupon
        Coupon flatCoupon = new Coupon();
        flatCoupon.setCode("FLAT100");
        flatCoupon.setDiscountType("FLAT");
        flatCoupon.setDiscountValue(100.0);
        flatCoupon.setMinimumOrder(500.0);
        flatCoupon.setStatus(true);

        assertEquals(100.0, flatCoupon.calculateDiscount(800.0), 0.001);
    }
}
