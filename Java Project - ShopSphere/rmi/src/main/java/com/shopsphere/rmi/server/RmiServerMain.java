package com.shopsphere.rmi.server;

import com.shopsphere.rmi.remote.InventoryService;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * RMI Server Bootstrap creating the RMI Registry and registering the remote object.
 */
public class RmiServerMain {

    public static final int REGISTRY_PORT = 1099;
    public static final String SERVICE_NAME = "ShopSphereInventoryService";

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  ShopSphere Java RMI Server Bootstrap");
        System.out.println("==================================================");

        try {
            // Start local RMI registry programmatically
            try {
                LocateRegistry.createRegistry(REGISTRY_PORT);
                System.out.println("[RMI] Created internal RMI Registry on port " + REGISTRY_PORT);
            } catch (Exception e) {
                System.out.println("[RMI] Existing registry detected on port " + REGISTRY_PORT);
            }

            InventoryService service = new InventoryServiceImpl();
            String bindingUrl = "rmi://localhost:" + REGISTRY_PORT + "/" + SERVICE_NAME;
            Naming.rebind(bindingUrl, service);

            System.out.println("[RMI] Remote object bound successfully to: " + bindingUrl);
            System.out.println("[RMI] Server is ready to receive remote method invocations.");
        } catch (Exception e) {
            System.err.println("[RMI] Bootstrap failure: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
