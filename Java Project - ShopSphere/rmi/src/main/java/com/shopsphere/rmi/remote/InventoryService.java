package com.shopsphere.rmi.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface for the ShopSphere Distributed Inventory Service.
 * Fulfills RTU Syllabus RMI requirement.
 */
public interface InventoryService extends Remote {

    ProductDTO getProduct(int id) throws RemoteException;

    int getStock(int productId) throws RemoteException;

    boolean updateStock(int productId, int quantity) throws RemoteException;
}
