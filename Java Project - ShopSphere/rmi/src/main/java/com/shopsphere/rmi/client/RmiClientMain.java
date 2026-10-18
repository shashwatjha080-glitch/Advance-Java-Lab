package com.shopsphere.rmi.client;

import com.shopsphere.rmi.remote.InventoryService;
import com.shopsphere.rmi.remote.ProductDTO;

import java.rmi.Naming;

/**
 * Java RMI Client performing remote method invocation against ShopSphereInventoryService.
 */
public class RmiClientMain {

    public static void main(String[] args) {
        String serviceUrl = "rmi://localhost:1099/ShopSphereInventoryService";
        System.out.println("Looking up Remote Service at: " + serviceUrl);

        try {
            InventoryService service = (InventoryService) Naming.lookup(serviceUrl);
            System.out.println("[RMI Client] Successfully obtained remote reference stub!");

            // 1. Remote call getProduct
            int testProductId = 1;
            System.out.println("\n--- Invoking Remote: getProduct(" + testProductId + ") ---");
            ProductDTO product = service.getProduct(testProductId);
            if (product != null) {
                System.out.println("[RMI Result] " + product);
            } else {
                System.out.println("[RMI Result] Product not found.");
            }

            // 2. Remote call getStock
            System.out.println("\n--- Invoking Remote: getStock(" + testProductId + ") ---");
            int currentStock = service.getStock(testProductId);
            System.out.println("[RMI Result] Current Stock: " + currentStock + " units");

            // 3. Remote call updateStock (+5 units)
            System.out.println("\n--- Invoking Remote: updateStock(" + testProductId + ", +5) ---");
            boolean updated = service.updateStock(testProductId, 5);
            System.out.println("[RMI Result] Stock Increment Status: " + updated);

            int newStock = service.getStock(testProductId);
            System.out.println("[RMI Result] Verified Updated Stock: " + newStock + " units");

            // 4. Test validation: Attempt invalid negative stock update
            System.out.println("\n--- Invoking Remote Validation: updateStock(" + testProductId + ", -9999) ---");
            boolean rejected = service.updateStock(testProductId, -9999);
            System.out.println("[RMI Result] Correctly Rejected Negative Stock? " + (!rejected));

            System.out.println("\nJava RMI Distributed Execution Demonstration Completed Successfully!");
        } catch (Exception e) {
            System.err.println("[RMI Client] Lookup or invocation failed: " + e.getMessage());
            System.err.println("Ensure RmiServerMain is running first: mvn exec:java -pl rmi");
        }
    }
}
