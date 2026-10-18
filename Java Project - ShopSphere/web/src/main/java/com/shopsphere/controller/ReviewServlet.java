package com.shopsphere.controller;

import com.shopsphere.dao.ReviewDAO;
import com.shopsphere.model.Review;
import com.shopsphere.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller handling product review and rating submissions.
 */
@WebServlet(name = "ReviewServlet", urlPatterns = {"/add-review"})
public class ReviewServlet extends HttpServlet {

    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String reviewText = request.getParameter("reviewText");

            rating = Math.max(1, Math.min(5, rating));

            Review review = new Review();
            review.setUserId(user.getUserId());
            review.setProductId(productId);
            review.setRating(rating);
            review.setReviewText(reviewText != null ? reviewText.trim() : "");

            reviewDAO.addReview(review);
            session.setAttribute("flashSuccess", "Thank you! Your review has been submitted.");
            response.sendRedirect(request.getContextPath() + "/product-details?id=" + productId);
        } catch (Exception e) {
            session.setAttribute("flashError", "Failed to submit review: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }
}
