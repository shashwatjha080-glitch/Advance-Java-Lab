package com.shopsphere.controller;

import com.shopsphere.dao.ReviewDAO;
import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.model.Review;
import com.shopsphere.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller handling product browsing, search, category filtering,
 * product details, customer reviews, and recently viewed cookie management.
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/products", "/product-details"})
public class ProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if ("/product-details".equals(servletPath)) {
            handleProductDetails(request, response);
        } else {
            handleProductCatalog(request, response);
        }
    }

    private void handleProductCatalog(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String catParam = request.getParameter("category");
        String searchQuery = request.getParameter("q");
        String sortBy = request.getParameter("sort");
        String pageParam = request.getParameter("page");

        Integer categoryId = null;
        if (catParam != null && !catParam.trim().isEmpty()) {
            try {
                categoryId = Integer.parseInt(catParam.trim());
            } catch (NumberFormatException ignored) {}
        }

        int page = 1;
        if (pageParam != null) {
            try {
                page = Math.max(1, Integer.parseInt(pageParam.trim()));
            } catch (NumberFormatException ignored) {}
        }
        int pageSize = 12;

        try {
            List<Product> products = productService.getProducts(categoryId, searchQuery, sortBy, page, pageSize);
            int totalProducts = productService.getProductCount(categoryId, searchQuery);
            int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
            List<Category> categories = productService.getActiveCategories();

            request.setAttribute("products", products);
            request.setAttribute("categories", categories);
            request.setAttribute("selectedCategory", categoryId);
            request.setAttribute("searchQuery", searchQuery);
            request.setAttribute("sortBy", sortBy);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalProducts", totalProducts);

            request.getRequestDispatcher("/products.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Unable to load products: " + e.getMessage());
            request.getRequestDispatcher("/products.jsp").forward(request, response);
        }
    }

    private void handleProductDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        try {
            int productId = Integer.parseInt(idParam.trim());
            Product product = productService.getProductById(productId);

            if (product == null) {
                request.setAttribute("errorMessage", "Requested product was not found.");
                request.getRequestDispatcher("/products.jsp").forward(request, response);
                return;
            }

            List<Review> reviews = reviewDAO.findByProductId(productId);
            double avgRating = reviewDAO.getAverageRating(productId);

            request.setAttribute("product", product);
            request.setAttribute("reviews", reviews);
            request.setAttribute("averageRating", avgRating);
            request.setAttribute("reviewCount", reviews.size());

            // Related products in the same category
            List<Product> related = productService.getProducts(product.getCategoryId(), null, null, 1, 4);
            related.removeIf(p -> p.getProductId() == product.getProductId());
            request.setAttribute("relatedProducts", related);

            // Syllabus demonstration: Track recently viewed products in cookies
            updateRecentlyViewedCookie(request, response, productId);

            request.getRequestDispatcher("/product-details.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }

    private void updateRecentlyViewedCookie(HttpServletRequest request, HttpServletResponse response, int productId) {
        String existing = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("recentProducts".equals(c.getName())) {
                    existing = c.getValue();
                    break;
                }
            }
        }

        List<String> list = new ArrayList<>();
        if (!existing.isEmpty()) {
            list.addAll(Arrays.asList(existing.split("-")));
        }
        list.remove(String.valueOf(productId));
        list.add(0, String.valueOf(productId));
        if (list.size() > 5) {
            list = list.subList(0, 5);
        }

        String updated = String.join("-", list);
        Cookie recentCookie = new Cookie("recentProducts", updated);
        recentCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
        recentCookie.setPath(request.getContextPath() + "/");
        response.addCookie(recentCookie);
    }
}
