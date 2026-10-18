package com.shopsphere.listener;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.model.Category;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Enterprise Application Lifecycle Listener demonstrating ServletContextListener
 * and HttpSessionListener as required by the RTU syllabus.
 */
@WebListener
public class AppListener implements ServletContextListener, HttpSessionListener {

    private static final AtomicInteger activeSessions = new AtomicInteger(0);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        System.out.println("[AppListener] ==================================================");
        System.out.println("[AppListener] ShopSphere Web Application Initialized Successfully");
        System.out.println("[AppListener] Servlet Container Info: " + context.getServerInfo());
        System.out.println("[AppListener] ==================================================");

        context.setAttribute("appName", "ShopSphere");
        context.setAttribute("appVersion", "1.0.0-ENTERPRISE");
        context.setAttribute("appSupportEmail", "support@shopsphere.com");

        // Preload active categories into application context for high performance navbar rendering
        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            List<Category> categories = categoryDAO.findAll(true);
            context.setAttribute("globalCategories", categories);
            System.out.println("[AppListener] Preloaded " + categories.size() + " active categories into application scope.");
        } catch (Exception e) {
            System.err.println("[AppListener] Notice: Database not immediately reachable at startup. Global categories will load dynamically: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[AppListener] ShopSphere Web Application is shutting down cleanly.");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int count = activeSessions.incrementAndGet();
        se.getSession().getServletContext().setAttribute("activeSessionsCount", count);
        System.out.println("[AppListener] HttpSession created: " + se.getSession().getId() + " | Current active sessions: " + count);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int count = activeSessions.decrementAndGet();
        se.getSession().getServletContext().setAttribute("activeSessionsCount", Math.max(0, count));
        System.out.println("[AppListener] HttpSession destroyed: " + se.getSession().getId() + " | Current active sessions: " + count);
    }

    public static int getActiveSessionsCount() {
        return activeSessions.get();
    }
}
