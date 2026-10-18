package com.shopsphere.controller.admin;

import com.shopsphere.dao.CouponDAO;
import com.shopsphere.model.Coupon;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller managing promotional discount coupons.
 */
@WebServlet(name = "AdminCouponServlet", urlPatterns = {"/admin/coupons"})
public class AdminCouponServlet extends HttpServlet {

    private final CouponDAO couponDAO = new CouponDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Coupon> coupons = couponDAO.findAll();
            request.setAttribute("coupons", coupons);
            request.getRequestDispatcher("/admin/coupons.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error loading coupons: " + e.getMessage());
            request.getRequestDispatcher("/admin/coupons.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        try {
            if ("create".equalsIgnoreCase(action)) {
                Coupon c = new Coupon();
                c.setCode(request.getParameter("code"));
                c.setDiscountType(request.getParameter("discountType"));
                c.setDiscountValue(Double.parseDouble(request.getParameter("discountValue")));
                c.setMinimumOrder(Double.parseDouble(request.getParameter("minimumOrder")));
                c.setMaximumDiscount(Double.parseDouble(request.getParameter("maximumDiscount")));
                c.setExpiryDate(Date.valueOf(request.getParameter("expiryDate")));
                c.setStatus(request.getParameter("status") != null);

                couponDAO.create(c);
                session.setAttribute("flashSuccess", "Coupon created successfully.");
            } else if ("delete".equalsIgnoreCase(action)) {
                int couponId = Integer.parseInt(request.getParameter("couponId"));
                couponDAO.delete(couponId);
                session.setAttribute("flashSuccess", "Coupon deleted.");
            }
        } catch (Exception e) {
            session.setAttribute("flashError", "Coupon operation failed: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/coupons");
    }
}
