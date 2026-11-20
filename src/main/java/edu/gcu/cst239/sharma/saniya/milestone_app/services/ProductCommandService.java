package edu.gcu.cst239.sharma.saniya.milestone_app.services;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import edu.gcu.cst239.sharma.saniya.milestone_app.services.InventoryManager;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.InventoryItem;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.Product;



/**
 * Service class to process product-related commands and return JSON responses.
 */
public class ProductCommandService {

    private final InventoryManager inventoryManager;

    // Source - https://stackoverflow.com/a/64220515
    // Posted by ptomli, modified by community. See post 'Timeline' for change history
    // Retrieved 2026-11-20, License - CC BY-SA 4.0
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


    /**
     * Constructor for ProductCommandService.
     *
     * @param inventoryManager The InventoryManager instance to manage inventory items.
     */
    public ProductCommandService(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    /**
     * Processes a command message and returns a JSON response.
     *
     * @param message The command message to process.
     * @return A JSON string representing the result of the command.
     */
    public String processMessage(String message) {
        if (message == null || message.isBlank()) {
            return error("Command is required");
        }

        String[] parts = message.trim().split("\\s+");
        String command = parts[0].toUpperCase();

        try {
            return switch (command) {
                case "ALL" -> json(inventoryManager.getAllInventoryItems());
                case "GET" -> processGet(parts);
                case "ADD" -> processAdd(parts);
                case "DELETE" -> processDelete(parts);
                case "FIND" -> processFind(parts);
                case "SORT" -> processSort(parts);
                default -> error("Unknown command");
            };
        } catch (IllegalArgumentException ex) {
            return error(ex.getMessage());
        }
    }

    /**
     * Processes the SORT command and returns a JSON response.
     *
     * @param parts The command parts, where parts[1] is the field to sort by.
     * @return A JSON string representing the sorted inventory items or an error message.
     */
    private String processSort(String[] parts) {

        List<InventoryItem> sortedItems = new ArrayList<>();

        if (parts[1].equalsIgnoreCase("NAME")) {
            sortedItems = inventoryManager.getInventoryItemsSortedByName();
            return json(sortedItems);
        } else if (parts[1].equalsIgnoreCase("PRICE")) {
            sortedItems = inventoryManager.getInventoryItemsSortedByPrice();
            return json(sortedItems);
        } else {
            return error("Invalid field for sorting. Valid fields are: name, price, dateOfManufacture");
        }
    }

    /**
     * Processes the GET command and returns a JSON response.
     *
     * @param parts The command parts, where parts[1] is the product ID.
     * @return A JSON string representing the inventory item or an error message.
     */
    private String processGet(String[] parts) {
        requireLength(parts, 2, "Usage: GET id");
        InventoryItem item = inventoryManager.getInventoryItemByProductId(parseInt(parts[1], "Invalid id"));
        // Person person = dataService.getPerson(parseInt(parts[1], "Invalid id"));
        return json(item == null ? List.of() : List.of(item));
    }
 
    /**
     * Processes the ADD command and returns a JSON response.
     *
     * @param parts The command parts, where parts[1] to parts[7] represent product details and quantity.
     * @return A JSON string representing the added inventory item or an error message.
     */
    private String processAdd(String[] parts) {
        requireLength(parts, 8, "Usage: ADD id name description dateOfManufacture price category quantity");
        Product product = Product.builder()
                .id(parseInt(parts[1], "Invalid id"))
                .name(parts[2])
                .description(parts[3])
                .dateOfManufacture(parseDate(parts[4], "Invalid date"))
                .price(Double.parseDouble(parts[5]))
                .category(parts[6])
                .build();
        

        InventoryItem newItem = new InventoryItem( product, parseInt(parts[7], "Invalid quantity"));

        if(!inventoryManager.addInventoryItem(newItem)) {
            return error("Failed to add inventory item");
        }

        return json(List.of(newItem));
    }

    /**
     * Processes the DELETE command and returns a JSON response.
     *
     * @param parts The command parts, where parts[1] is the product ID.
     * @return A JSON string representing the result of the delete operation or an error message.
     */
    private String processDelete(String[] parts) {
        requireLength(parts, 2, "Usage: DELETE id");
        // Person removed = dataService.deletePerson(parseInt(parts[1], "Invalid id"));
        boolean removed = inventoryManager.removeProductById(parseInt(parts[1], "Invalid id"));

        return json(removed == false ? List.of() : List.of());
    }

    /**
     * Processes the FIND command and returns a JSON response.
     *
     * @param parts The command parts, where parts[1] is the product name.
     * @return A JSON string representing the found inventory items or an error message.
     */
    private String processFind(String[] parts) {
        requireLength(parts, 2, "Usage: FIND name");
        List<InventoryItem> found = inventoryManager.searchProductsByName(parts[1]);
        return json(found);
    }

    /**
     * Parses a string into an integer, throwing an exception with a custom error message if parsing fails.
     *
     * @param text The string to parse.
     * @param errorMessage The error message to use if parsing fails.
     * @return The parsed integer.
     * @throws IllegalArgumentException if the string cannot be parsed into an integer.
     */
    private int parseInt(String text, String errorMessage) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Parses a string into a LocalDate, throwing an exception with a custom error message if parsing fails.
     *
     * @param text The string to parse.
     * @param errorMessage The error message to use if parsing fails.
     * @return The parsed LocalDate.
     * @throws IllegalArgumentException if the string cannot be parsed into a LocalDate.
     */
    private LocalDate parseDate(String text, String errorMessage) {
        try {
            return LocalDate.parse(text);
        } catch (Exception ex) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Ensures that the length of the command parts matches the expected length, throwing an exception with a custom error message if it does not.
     *
     * @param parts The command parts to check.
     * @param length The expected length of the command parts.
     * @param message The error message to use if the lengths do not match.
     * @throws IllegalArgumentException if the lengths do not match.
     */
    private void requireLength(String[] parts, int length, String message) {
        if (parts.length != length) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Converts a list of InventoryItem objects into a JSON string.
     * @param items The list of InventoryItem objects to convert.
     * @return A JSON string representing the list of inventory items.
     */
    private String json(List<InventoryItem> items) {
        try {
            return objectMapper.writeValueAsString(items);
        } catch (JsonProcessingException ex) {
            return error("Unable to create JSON");
        }
    }

    /**
     * Converts an error message into a JSON string.
     * @param message The error message to convert.
     * @return A JSON string representing the error message.
     */
    private String error(String message) {
        try {
            return objectMapper.writeValueAsString(List.of("ERROR: " + message));
        } catch (JsonProcessingException ex) {
            return "[\"ERROR\"]";
        }
    }
}
