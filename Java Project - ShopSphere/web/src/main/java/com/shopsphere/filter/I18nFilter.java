package com.shopsphere.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.jstl.core.Config;

import java.io.IOException;
import java.util.Locale;

/**
 * Filter setting UTF-8 encoding and managing Internationalization (i18n) Locale.
 */
@WebFilter(filterName = "I18nFilter", urlPatterns = {"/*"})
public class I18nFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String lang = httpRequest.getParameter("lang");
        HttpSession session = httpRequest.getSession(true);

        if (lang != null && !lang.trim().isEmpty()) {
            Locale locale = "hi".equalsIgnoreCase(lang) ? new Locale("hi") : Locale.ENGLISH;
            session.setAttribute("lang", lang.toLowerCase());
            session.setAttribute("currentLocale", locale);
            Config.set(session, Config.FMT_LOCALE, locale);
        } else if (session.getAttribute("currentLocale") == null) {
            Locale defaultLocale = Locale.ENGLISH;
            session.setAttribute("lang", "en");
            session.setAttribute("currentLocale", defaultLocale);
            Config.set(session, Config.FMT_LOCALE, defaultLocale);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
