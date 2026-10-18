package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String name = request.getParameter("name");
        if (name != null && !name.trim().isEmpty()) {
            out.println("<h1>Welcome " + name + "</h1>");
        } else {
            out.println("<h1>Hello Students!</h1>");
            out.println("<h2>Welcome to Servlet Programming</h2>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String course = request.getParameter("course");
        PrintWriter out = response.getWriter();
        out.println("<h1>Welcome " + (name != null ? name : "") + "</h1>");
        if (email != null && !email.trim().isEmpty()) {
            out.println("<p>Email: " + email + "</p>");
        }
        if (course != null && !course.trim().isEmpty()) {
            out.println("<p>Course: " + course + "</p>");
        }
        out.println("<br><a href='index.html'>Back to Home</a>");
    }
}