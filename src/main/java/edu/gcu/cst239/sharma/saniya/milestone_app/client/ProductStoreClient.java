package edu.gcu.cst239.sharma.saniya.milestone_app.client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * 
 * ProductStoreClient simple client application that connects to the ProductStoreServer and 
 * allows users to send commands to manage products in the inventory. 
 * It provides a command-line interface for users to interact with the server.
 * 
 */


public class ProductStoreClient {
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5050;

        try (Socket socket = new Socket(host, port);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to " + host + ":" + port);
            System.out.println("Commands: ALL, GET id, ADD id name description dateOfManufacture price category quantity");
            System.out.println("DELETE id, FIND name, SORT NAME/PRICE or . to quit");

            while (true) {
                System.out.print("> ");
                String command = scanner.nextLine();
                output.println(command);
                System.out.println(input.readLine());
                if (command.equals(".")) {
                    break;
                }
            }
        } catch (IOException ex) {
            System.err.println("Client error: " + ex.getMessage());
        }
    }
}
 
    

