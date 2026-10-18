package com.shopsphere.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Enterprise Database Utility for ShopSphere.
 * Supports JNDI DataSource lookup with graceful fallback to JDBC DriverManager
 * loaded from classpath properties (db.properties) and environment variable overrides.
 */
public class DBUtil {

    private static String driverClassName = "com.mysql.cj.jdbc.Driver";
    private static String jdbcUrl = "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
    private static String dbUser = "root";
    private static String dbPassword = "root";
    private static DataSource jndiDataSource = null;

    static {
        // 1. Try JNDI DataSource (Standard for Tomcat production deployments)
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            jndiDataSource = (DataSource) envCtx.lookup("jdbc/ShopSphereDB");
            System.out.println("[DBUtil] Initialized via JNDI DataSource: jdbc/ShopSphereDB");
        } catch (NamingException e) {
            // JNDI not available (e.g. running outside full container or during CLI testing)
            jndiDataSource = null;
        }

        // 2. Load configurations from db.properties
        try (InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                if (props.getProperty("db.driver") != null) {
                    driverClassName = props.getProperty("db.driver").trim();
                }
                if (props.getProperty("db.url") != null) {
                    jdbcUrl = props.getProperty("db.url").trim();
                }
                if (props.getProperty("db.user") != null) {
                    dbUser = props.getProperty("db.user").trim();
                }
                if (props.getProperty("db.password") != null) {
                    dbPassword = props.getProperty("db.password").trim();
                }
            }
        } catch (Exception e) {
            System.err.println("[DBUtil] Warning: Unable to load db.properties: " + e.getMessage());
        }

        // 3. Allow environment variable overrides
        String envUrl = System.getenv("SHOPSPHERE_DB_URL");
        if (envUrl != null && !envUrl.isEmpty()) {
            jdbcUrl = envUrl;
        }
        String envUser = System.getenv("SHOPSPHERE_DB_USER");
        if (envUser != null && !envUser.isEmpty()) {
            dbUser = envUser;
        }
        String envPass = System.getenv("SHOPSPHERE_DB_PASSWORD");
        if (envPass != null) {
            dbPassword = envPass;
        }

        // 4. Register JDBC driver class
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            System.err.println("[DBUtil] MySQL Driver Class Not Found: " + driverClassName + " (" + e.getMessage() + ")");
        }
    }

    /**
     * Obtains a new database connection.
     */
    public static Connection getConnection() throws SQLException {
        if (jndiDataSource != null) {
            try {
                return jndiDataSource.getConnection();
            } catch (SQLException e) {
                System.err.println("[DBUtil] JNDI connection attempt failed, falling back to direct JDBC: " + e.getMessage());
            }
        }
        return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
    }

    /**
     * Closes the connection safely.
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {}
        }
    }

    /**
     * Closes statement and connection safely.
     */
    public static void close(Statement stmt, Connection conn) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ignored) {}
        }
        close(conn);
    }

    /**
     * Closes resultSet, statement, and connection safely.
     */
    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {}
        }
        close(stmt, conn);
    }

    public static String getJdbcUrl() {
        return jdbcUrl;
    }

    public static String getDbUser() {
        return dbUser;
    }
}
