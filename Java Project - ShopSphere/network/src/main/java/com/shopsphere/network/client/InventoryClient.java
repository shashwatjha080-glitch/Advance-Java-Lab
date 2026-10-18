package com.shopsphere.network.client;

import com.shopsphere.network.model.ProductDTO;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Socket Client demonstrating client-server interaction and object deserialization.
 */
public class InventoryClient {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 8888;

        System.out.println("Connecting to ShopSphere Inventory Socket Server at " + host + ":" + port + "...");

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            // Read welcome banner
            System.out.println("[Server]: " + in.readLine());

            // 1. Test GET_STOCK
            System.out.println("\n[Client -> Server]: GET_STOCK 1");
            out.println("GET_STOCK 1");
            System.out.println("[Server -> Client]: " + in.readLine());

            // 2. Test SEARCH_PRODUCT
            System.out.println("\n[Client -> Server]: SEARCH_PRODUCT Headphone");
            out.println("SEARCH_PRODUCT Headphone");
            String countLine = in.readLine();
            System.out.println("[Server -> Client]: " + countLine);
            if (countLine.startsWith("MATCH_COUNT")) {
                int count = Integer.parseInt(countLine.split(" ")[1]);
                for (int i = 0; i < count; i++) {
                    System.out.println("   " + in.readLine());
                }
            }

            // 3. Test Object Deserialization via GET_PRODUCT
            System.out.println("\n[Client -> Server]: GET_PRODUCT 2 (Object Serialization Demonstration)");
            out.println("GET_PRODUCT 2");
            String responseHeader = in.readLine();
            if ("OBJECT_FOLLOWING".equals(responseHeader)) {
                Object obj = ois.readObject();
                if (obj instanceof ProductDTO) {
                    ProductDTO dto = (ProductDTO) obj;
                    System.out.println("[Client]: Successfully deserialized ProductDTO object!");
                    System.out.println("   ID: " + dto.getProductId());
                    System.out.println("   Name: " + dto.getName());
                    System.out.println("   Brand: " + dto.getBrand());
                    System.out.println("   Price: ₹" + dto.getPrice());
                    System.out.println("   Stock: " + dto.getStock() + " units");
                }
            }

            // 4. Disconnect cleanly
            System.out.println("\n[Client -> Server]: QUIT");
            out.println("QUIT");
            System.out.println("[Server -> Client]: " + in.readLine());

            System.out.println("\nJava Socket Networking & Serialization Demonstration completed successfully!");
        } catch (Exception e) {
            System.err.println("[Client] Connection error: " + e.getMessage());
            System.err.println("Ensure InventoryServer is running first: mvn exec:java -pl network");
        }
    }
}
