package com.shopsphere.controller.admin;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller managing Admin Product CRUD and Inventory Updates.
 */
@WebServlet(name = "AdminProductServlet", urlPatterns = {"/admin/products"})
public class AdminProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Product> products = productService.getAllProductsForAdmin();
            List<Category> categories = productService.getAllCategories();

            request.setAttribute("products", products);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/admin/products.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error loading products: " + e.getMessage());
            request.getRequestDispatcher("/admin/products.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        try {
            if ("create".equalsIgnoreCase(action) || "update".equalsIgnoreCase(action)) {
                Product p = new Product();
                if ("update".equalsIgnoreCase(action)) {
                    p.setProductId(Integer.parseInt(request.getParameter("productId")));
                }
                p.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
                p.setName(request.getParameter("name"));
                p.setBrand(request.getParameter("brand"));
                p.setDescription(request.getParameter("description"));
                p.setPrice(Double.parseDouble(request.getParameter("price")));
                p.setDiscount(Double.parseDouble(request.getParameter("discount")));
                p.setStock(Integer.parseInt(request.getParameter("stock")));
                p.setImageUrl(request.getParameter("imageUrl"));
                p.setStatus(request.getParameter("status") != null);

                productService.saveProduct(p);
                session.setAttribute("flashSuccess", "Product " + ("create".equalsIgnoreCase(action) ? "created" : "updated") + " successfully.");
            } else if ("delete".equalsIgnoreCase(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                productService.deleteProduct(productId);
                session.setAttribute("flashSuccess", "Product deleted successfully.");
            } else if ("stock".equalsIgnoreCase(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int stock = Integer.parseInt(request.getParameter("stock"));
                productService.updateStock(productId, stock);
                session.setAttribute("flashSuccess", "Inventory stock updated.");
            }
        } catch (Exception e) {
            session.setAttribute("flashError", "Product operation failed: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
}
