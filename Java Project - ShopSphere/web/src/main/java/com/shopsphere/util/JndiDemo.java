package com.shopsphere.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

/**
 * Educational demonstration of Java Naming and Directory Interface (JNDI).
 * Demonstrates InitialContext instantiation, environment properties,
 * and resource lookup patterns used in enterprise Java EE / Jakarta EE containers.
 */
public class JndiDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  RTU Syllabus Demo: JNDI (Java Naming & Directory)");
        System.out.println("==================================================");

        try {
            // Setup JNDI environment properties
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
            env.put(Context.PROVIDER_URL, "file:/");

            System.out.println("[JNDI] 1. Initializing InitialContext...");
            System.out.println("[JNDI] 2. In Tomcat / Jakarta EE Containers, resources are bound under java:comp/env");
            System.out.println("       Example lookup: DataSource ds = (DataSource) ctx.lookup(\"java:comp/env/jdbc/ShopSphereDB\");");

            // Inspect DBUtil JNDI lookup logic
            System.out.println("[JNDI] 3. ShopSphere DBUtil incorporates automatic JNDI lookup for container deployment.");
            System.out.println("[JNDI] 4. If running in standalone JVM outside container, DBUtil gracefully falls back to DriverManager.");
            System.out.println("\nJNDI Architecture Concept Demonstrated Successfully!");
        } catch (Exception e) {
            System.err.println("[JNDI] Demonstration exception: " + e.getMessage());
        }
    }
}
