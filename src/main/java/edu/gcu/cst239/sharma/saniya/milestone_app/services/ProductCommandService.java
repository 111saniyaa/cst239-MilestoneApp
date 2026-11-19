package edu.gcu.cst239.sharma.saniya.milestone_app.services;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.gcu.cst239.sharma.saniya.milestone_app.services.InventoryManager;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.InventoryItem;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.Product;

public class ProductCommandService {

    private final InventoryManager inventoryManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProductCommandService(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

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

    private String processGet(String[] parts) {
        requireLength(parts, 2, "Usage: GET id");
        InventoryItem item = inventoryManager.getInventoryItemByProductId(parseInt(parts[1], "Invalid id"));
        // Person person = dataService.getPerson(parseInt(parts[1], "Invalid id"));
        return json(item == null ? List.of() : List.of(item));
    }
 
    private String processAdd(String[] parts) {
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

    private String processDelete(String[] parts) {
        requireLength(parts, 2, "Usage: DELETE id");
        // Person removed = dataService.deletePerson(parseInt(parts[1], "Invalid id"));
        boolean removed = inventoryManager.removeProductById(parseInt(parts[1], "Invalid id"));

        return json(removed == false ? List.of() : List.of());
    }

    private String processFind(String[] parts) {
        requireLength(parts, 2, "Usage: FIND name");
        List<InventoryItem> found = inventoryManager.searchProductsByName(parts[1]);
        return json(found);
    }

    private int parseInt(String text, String errorMessage) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private LocalDate parseDate(String text, String errorMessage) {
        try {
            return LocalDate.parse(text);
        } catch (Exception ex) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void requireLength(String[] parts, int length, String message) {
        if (parts.length != length) {
            throw new IllegalArgumentException(message);
        }
    }

    private String json(List<InventoryItem> items) {
        try {
            return objectMapper.writeValueAsString(items);
        } catch (JsonProcessingException ex) {
            return error("Unable to create JSON");
        }
    }

    private String error(String message) {
        try {
            return objectMapper.writeValueAsString(List.of("ERROR: " + message));
        } catch (JsonProcessingException ex) {
            return "[\"ERROR\"]";
        }
    }
}
