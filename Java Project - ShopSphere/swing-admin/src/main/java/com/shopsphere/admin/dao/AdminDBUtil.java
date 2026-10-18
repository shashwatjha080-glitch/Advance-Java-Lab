package com.shopsphere.admin.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * JDBC Connection Manager for the Desktop Swing Administration module.
 */
public class AdminDBUtil {

    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
    private static String user = "root";
    private static String password = "Project@1221";

    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("[AdminDBUtil] MySQL Driver Class Not Found: " + e.getMessage());
        }

        String envUrl = System.getenv("SHOPSPHERE_DB_URL");
        if (envUrl != null && !envUrl.isEmpty()) url = envUrl;

        String envUser = System.getenv("SHOPSPHERE_DB_USER");
        if (envUser != null && !envUser.isEmpty()) user = envUser;

        String envPass = System.getenv("SHOPSPHERE_DB_PASSWORD");
        if (envPass != null) password = envPass;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException ignored) {}
        }
    }

    public static void close(Statement stmt, Connection conn) {
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException ignored) {}
        }
        close(conn);
    }

    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException ignored) {}
        }
        close(stmt, conn);
    }
}
