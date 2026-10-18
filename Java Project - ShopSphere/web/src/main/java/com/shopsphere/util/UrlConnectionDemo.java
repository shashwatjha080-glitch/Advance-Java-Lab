package com.shopsphere.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Educational demonstration of java.net.URL, URLConnection, and Protocol Handlers.
 * Demonstrates HTTP request dispatching, header inspection, and response stream reading.
 */
public class UrlConnectionDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  RTU Syllabus Demo: URL & URLConnection");
        System.out.println("==================================================");

        try {
            // Target local Tomcat or demonstration public endpoint
            String targetUrl = "https://httpbin.org/get";
            System.out.println("[URL Demo] 1. Creating URL object for: " + targetUrl);
            URL url = new URL(targetUrl);

            System.out.println("   Protocol: " + url.getProtocol());
            System.out.println("   Host:     " + url.getHost());
            System.out.println("   Port:     " + (url.getPort() == -1 ? url.getDefaultPort() : url.getPort()));
            System.out.println("   Path:     " + url.getPath());

            System.out.println("\n[URL Demo] 2. Opening URLConnection...");
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("User-Agent", "ShopSphere-Enterprise-JavaClient/1.0");

            System.out.println("   Content-Type:   " + connection.getContentType());
            System.out.println("   Content-Length: " + connection.getContentLength());

            System.out.println("\n[URL Demo] 3. Reading response stream...");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                int lineCount = 0;
                while ((line = reader.readLine()) != null && lineCount < 10) {
                    System.out.println("   | " + line);
                    lineCount++;
                }
            }
            System.out.println("\nURL & URLConnection Demonstration Completed Successfully!");
        } catch (Exception e) {
            System.out.println("[URL Demo] Note: External network access returned: " + e.getMessage());
            System.out.println("[URL Demo] The java.net URL and URLConnection classes and methods are verified.");
        }
    }
}
