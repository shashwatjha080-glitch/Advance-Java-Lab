package com.shopsphere.service;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Category;
import com.shopsphere.model.Product;

import java.sql.SQLException;
import java.util.List;

/**
 * Service managing catalog presentation, category filtering, search, and inventory rules.
 */
public class ProductService {

    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
        this.categoryDAO = new CategoryDAO();
    }

    public ProductService(ProductDAO productDAO, CategoryDAO categoryDAO) {
        this.productDAO = productDAO;
        this.categoryDAO = categoryDAO;
    }

    public List<Product> getProducts(Integer categoryId, String searchQuery, String sortBy, int page, int pageSize) throws SQLException {
        int limit = pageSize > 0 ? pageSize : 12;
        int offset = (page > 1) ? (page - 1) * limit : 0;
        return productDAO.findAll(categoryId, searchQuery, sortBy, offset, limit);
    }

    public int getProductCount(Integer categoryId, String searchQuery) throws SQLException {
        return productDAO.countAll(categoryId, searchQuery);
    }

    public List<Product> getFeaturedProducts(int limit) throws SQLException {
        return productDAO.findFeatured(limit > 0 ? limit : 8);
    }

    public Product getProductById(int productId) throws SQLException {
        return productDAO.findById(productId);
    }

    public List<Category> getActiveCategories() throws SQLException {
        return categoryDAO.findAll(true);
    }

    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.findAll(false);
    }

    public List<Product> getAllProductsForAdmin() throws SQLException {
        return productDAO.findAllForAdmin();
    }

    public List<Product> getLowStockAlerts(int threshold) throws SQLException {
        return productDAO.findLowStock(threshold > 0 ? threshold : 10);
    }

    public boolean saveProduct(Product product) throws SQLException {
        if (product.getProductId() > 0) {
            return productDAO.update(product);
        } else {
            return productDAO.create(product);
        }
    }

    public boolean deleteProduct(int productId) throws SQLException {
        return productDAO.delete(productId);
    }

    public boolean updateStock(int productId, int newStock) throws SQLException {
        if (newStock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        return productDAO.setStock(productId, newStock);
    }
}
