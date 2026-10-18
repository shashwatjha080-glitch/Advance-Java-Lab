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
 * Filter protecting customer routes requiring authentication.
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {
        "/cart", "/cart.jsp",
        "/checkout", "/checkout.jsp",
        "/orders", "/orders.jsp",
        "/order-details", "/order-details.jsp",
        "/order-success", "/order-success.jsp",
        "/profile", "/profile.jsp",
        "/wishlist", "/wishlist.jsp"
})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            String requestURI = httpRequest.getRequestURI();
            String queryString = httpRequest.getQueryString();
            String target = (queryString != null) ? requestURI + "?" + queryString : requestURI;

            if (session == null) {
                session = httpRequest.getSession(true);
            }
            session.setAttribute("redirectAfterLogin", target);
            session.setAttribute("authError", "Please sign in to access your " + httpRequest.getServletPath().replace("/", "") + ".");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
