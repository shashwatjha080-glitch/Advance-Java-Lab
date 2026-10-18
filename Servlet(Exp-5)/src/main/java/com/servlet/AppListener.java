package com.servlet;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class AppListener implements ServletContextListener, HttpSessionListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("Application Started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("Application Stopped");
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        System.out.println("Session Created");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        System.out.println("Session Destroyed");
    }
}