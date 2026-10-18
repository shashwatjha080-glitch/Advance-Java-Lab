package com.shopsphere.controller.admin;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.model.Category;
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
 * Controller managing Category administration.
 */
@WebServlet(name = "AdminCategoryServlet", urlPatterns = {"/admin/categories"})
public class AdminCategoryServlet extends HttpServlet {

    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Category> categories = categoryDAO.findAll(false);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/admin/categories.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error loading categories: " + e.getMessage());
            request.getRequestDispatcher("/admin/categories.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        try {
            if ("create".equalsIgnoreCase(action)) {
                Category c = new Category();
                c.setName(request.getParameter("name"));
                c.setDescription(request.getParameter("description"));
                c.setStatus(request.getParameter("status") != null);
                categoryDAO.create(c);
                session.setAttribute("flashSuccess", "Category created successfully.");
            } else if ("update".equalsIgnoreCase(action)) {
                Category c = new Category();
                c.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
                c.setName(request.getParameter("name"));
                c.setDescription(request.getParameter("description"));
                c.setStatus(request.getParameter("status") != null);
                categoryDAO.update(c);
                session.setAttribute("flashSuccess", "Category updated successfully.");
            } else if ("delete".equalsIgnoreCase(action)) {
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                categoryDAO.delete(categoryId);
                session.setAttribute("flashSuccess", "Category deleted.");
            }
        } catch (Exception e) {
            session.setAttribute("flashError", "Category operation failed: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/categories");
    }
}
