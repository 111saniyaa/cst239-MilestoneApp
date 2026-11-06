package edu.gcu.cst239.sharma.saniya.milestone_app.services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.gcu.cst239.sharma.saniya.milestone_app.models.Product;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.ProductDto;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.InventoryItem;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.InventoryItemDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

public class ProductJsonService {
    
    public ProductJsonService() {
    }


    // object mapper for JSON serialization and deserialization
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public List<ProductDto> readProductDtos(Path filePath) {
        try {
            // Read the JSON file and convert it to a list of ProductDto objects
            return objectMapper.readValue( filePath.toFile(), new TypeReference<List<ProductDto>>() {});
        } catch (NoSuchFileException e) {
            System.err.println("File not found: " + filePath);
            return List.of(); // Return an empty list if the file is not found
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file: " + filePath, e);
        }
    }


    public List<InventoryItemDto> readInventoryItemsDtos(Path filePath) {
        try {
            // Read the JSON file and convert it to a list of InventoryItemDto objects
            return objectMapper.readValue(filePath.toFile(), new TypeReference<List<InventoryItemDto>>() {});
        } catch (NoSuchFileException e) {
            System.err.println("File not found: " + filePath);
            return List.of(); // Return an empty list if the file is not found
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file: " + filePath, e);
        }
    }

    public  List<Product> readProducts(Path filePath) {
        
        // First we will read the ProductDto objects from the JSON file, 
        List<ProductDto> productDtos = readProductDtos(filePath);

        System.out.println("IMPORTANT : Read " + productDtos.size() + " ProductDto objects from " + filePath);
        
        // then we will map them to Product objects using the ProductMapper.
        return productDtos.stream()
                .map(ProductMapper::toProduct)
                .toList();
    }


    public List<InventoryItem> readInventory(Path productFilePath, Path inventoryFilePath) {
        
        // Read the products from the product JSON file
        List<Product> products = readProducts(productFilePath);

        // Read the inventory items dto (just id and quantity) from the inventory JSON file
        List<InventoryItemDto> inventoryItemDtos = readInventoryItemsDtos(inventoryFilePath);


        // Map the InventoryItemDto to InventoryItem using the corresponding Product (based on product id)
        return inventoryItemDtos.stream()
                .map(dto -> {
                    Product product = products.stream()
                            .filter(p -> p.getId() == dto.id())
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Product with ID " + dto.id() + " not found"));
                    return new InventoryItem(product, dto.quantityInStock());
                })
                .toList();
    }


    // Write into the JSON file 


    public void writeProductDtos(Path filePath, List<ProductDto> productDtos) throws IOException {
        
        // Ensure the parent directory exists before writing to the file
        Path parent = filePath.toAbsolutePath().getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }

        // Write the list of ProductDto objects to the specified JSON file
        objectMapper.writeValue(filePath.toFile(), productDtos);
    }


    public void writeInventoryItemDtos(Path filePath, List<InventoryItemDto> inventoryItemDtos) throws IOException {
        
        // Ensure the parent directory exists before writing to the file
        Path parent = filePath.toAbsolutePath().getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }

        // Write the list of InventoryItemDto objects to the specified JSON file
        objectMapper.writeValue(filePath.toFile(), inventoryItemDtos);
    }


    public void writeInventoryItems(Path productFilePath, Path inventoryFilePath, List<InventoryItem> inventoryItems) throws IOException {
        
        // Convert InventoryItem objects to ProductDto for writing to JSON files
        List<ProductDto> productDtos = inventoryItems.stream()
                .map(InventoryItem::getProduct)
                .map(ProductMapper::toProductDto)
                .toList();

        writeProductDtos(productFilePath, productDtos);

        // Convert InventoryItem objects to InventoryItemDto for writing to JSON file
        List<InventoryItemDto> inventoryItemDtos = inventoryItems.stream()
                .map(item -> new InventoryItemDto(item.getProduct().getId(), item.getQuantityInStock()))
                .toList();
        writeInventoryItemDtos(inventoryFilePath, inventoryItemDtos);
    }

}
