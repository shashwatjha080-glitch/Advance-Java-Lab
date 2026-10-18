package com.shopsphere.filter;

import com.shopsphere.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter securing all administrative endpoints (/admin/*) against unauthorized access.
 */
@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !user.isAdmin()) {
            if (session == null) {
                session = httpRequest.getSession(true);
            }
            session.setAttribute("adminAuthError", "Access Denied: Administrator credentials required to access this portal.");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin-login.jsp");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
