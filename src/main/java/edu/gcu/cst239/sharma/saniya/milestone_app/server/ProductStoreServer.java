package edu.gcu.cst239.sharma.saniya.milestone_app.server;
    

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import edu.gcu.cst239.sharma.saniya.milestone_app.services.ProductCommandService;
import edu.gcu.cst239.sharma.saniya.milestone_app.services.InventoryManager;


/**
 * 
 * ProductStoreServer is a simple server application that listens for client connections and processes commands related to product inventory management.
 * It uses the ProductCommandService to handle incoming commands and manage the inventory through the InventoryManager.
 * 
 */

public class ProductStoreServer {
    private final ProductCommandService productCommandService;

    /**
     * 
     * @param productCommandService
     * @param inventoryManager
     */
    public ProductStoreServer(ProductCommandService productCommandService, InventoryManager inventoryManager) {
        this.productCommandService = productCommandService;
    }

    /**
     * 
     * Starts the server and listens for incoming client connections on the specified port.
     * For each connected client, it creates a new thread to handle communication.
     * @param port
     */
    public void start(int port) {
        System.out.println("Server listening on port " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException ex) {
            System.err.println("Server error: " + ex.getMessage());
        }
    }

    /**
     * 
     * Handles communication with a connected client.
     * @param socket the client socket
     */
    private void handleClient(Socket socket) {
        System.out.println("Client connected: " + socket.getRemoteSocketAddress());
        try (socket;
             BufferedReader input = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            String message;
            while ((message = input.readLine()) != null) {
                if (message.equals(".")) {
                    output.println("Goodbye");
                    break;
                }
                output.println(productCommandService.processMessage(message));
            }
        } catch (IOException ex) {
            System.err.println("Client error: " + ex.getMessage());
        }
        System.out.println("Client disconnected");
    }


    /**
     * 
     * The main method initializes the InventoryManager and ProductCommandService, then starts the ProductStoreServer on port 5050.
     * @param args
     */
    public static void main(String[] args) {
        InventoryManager inventoryManager = new InventoryManager();
        ProductCommandService productCommandService = new ProductCommandService(inventoryManager);
        new ProductStoreServer(productCommandService, inventoryManager).start(5050);
    }
}



