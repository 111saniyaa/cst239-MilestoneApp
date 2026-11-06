package edu.gcu.cst239.sharma.saniya.milestone_app.services;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import edu.gcu.cst239.sharma.saniya.milestone_app.models.ProductDto;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.InventoryItem;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.InventoryItemDto;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.Product;
import edu.gcu.cst239.sharma.saniya.milestone_app.services.ProductJsonService;

public class ProductJsonServiceTest {


    private static final String CONTROLLED_PRODUCTS_JSON = """
            [
              {
                "id": 11,
                "name": "Trail Kettle",
                "description": "Compact camping kettle",
                "dateOfManufacture": "2024-02-03",
                "price": 39.95,
                "category": "Outdoor"
              },
              {
                "id": 12,
                "name": "Steel Bottle",
                "description": "Insulated water bottle",
                "dateOfManufacture": "2024-05-17",
                "price": 24.50,
                "category": "Fitness"
              }
            ]
            """;

    private static final String CONTROLLED_INVENTORY_JSON = """
            [
              { "id": 11, "quantityInStock": 6 },
              { "id": 12, "quantityInStock": 4 }
            ]
            """;



    @TempDir Path tempDirectory;

    private final ProductJsonService productJsonService = new ProductJsonService();

    // json files from test resources
    private final Path productsFilePath = Path.of("src", "test", "resources", "data", "products.json");
    private final Path inventoryFilePath = Path.of("src", "test", "resources", "data", "inventory.json");


    @Test void resourceLoadsExactlyHundredProductDtos() {
        List<ProductDto> productDtos = productJsonService.readProductDtos(productsFilePath);
        assertEquals(100, productDtos.size(), "Expected 100 ProductDto objects in the products.json file");
    }


    @Test void resourceLoadsExactlyHundredInventoryItemDtos() {
        List<InventoryItemDto> inventoryItemDtos = productJsonService.readInventoryItemsDtos(inventoryFilePath);
        assertEquals(100, inventoryItemDtos.size(), "Expected 100 InventoryItemDto objects in the inventory.json file");
    }

    // Controlled test

    private Path writeTempFile(String fileName, String content) throws IOException {
        return Files.writeString(tempDirectory.resolve(fileName), content);
    }

    
    @Test void controlledJsonLoadsDtosAndDomainProducts() throws IOException {
        Path controlledProductsFile = writeTempFile("controlled-products.json", CONTROLLED_PRODUCTS_JSON);

        List<ProductDto> productDtos = productJsonService.readProductDtos(controlledProductsFile);
        List<Product> products = productJsonService.readProducts(controlledProductsFile);

        assertEquals(2, productDtos.size(), "Expected the two controlled ProductDto records");
        assertEquals(2, products.size(), "Expected the two controlled Product records");
        assertAll(
                () -> assertEquals(11, products.get(0).getId()),
                () -> assertEquals("Trail Kettle", products.get(0).getName()),
                () -> assertEquals("Compact camping kettle", products.get(0).getDescription()),
                () -> assertEquals(LocalDate.of(2024, 2, 3), products.get(0).getDateOfManufacture()),
                () -> assertEquals(39.95, products.get(0).getPrice(), 0.0001),
                () -> assertEquals("Outdoor", products.get(0).getCategory()));
    }

    @Test void controlledInventoryJsonLoadsDtosAndDomainInventoryItems() throws IOException {
        Path controlledProductsFile = writeTempFile("inventory-products.json", CONTROLLED_PRODUCTS_JSON);
        Path controlledInventoryFile = writeTempFile("controlled-inventory.json", CONTROLLED_INVENTORY_JSON);

        List<InventoryItemDto> inventoryItemDtos = productJsonService.readInventoryItemsDtos(controlledInventoryFile);
        List<InventoryItem> inventoryItems = productJsonService.readInventory(
                controlledProductsFile, controlledInventoryFile);

        assertEquals(2, inventoryItemDtos.size());
        assertEquals(2, inventoryItems.size());
        assertAll(
                () -> assertEquals(11, inventoryItems.get(0).getProduct().getId()),
                () -> assertEquals("Trail Kettle", inventoryItems.get(0).getProduct().getName()),
                () -> assertEquals(6, inventoryItems.get(0).getQuantityInStock()),
                () -> assertEquals(12, inventoryItems.get(1).getProduct().getId()),
                () -> assertEquals(4, inventoryItems.get(1).getQuantityInStock()));
    }

    @Test void savingAndReloadingPreservesProductValuesAndStock() throws IOException {
        Path savedProductsFile = tempDirectory.resolve("saved-products.json");
        Path savedInventoryFile = tempDirectory.resolve("saved-inventory.json");
        List<InventoryItem> itemsToSave = List.of(
                TestData.inventoryItem(
                        21, "Lantern", "Rechargeable camping lantern", LocalDate.of(2025, 1, 9), 42.75, 8, "Outdoor"),
                TestData.inventoryItem(
                        22, "Lunch Box", "Insulated lunch box", LocalDate.of(2025, 2, 14), 18.25, 4, "Kitchen"));

        productJsonService.writeInventoryItems(savedProductsFile, savedInventoryFile, itemsToSave);

        List<ProductDto> savedProductDtos = productJsonService.readProductDtos(savedProductsFile);
        List<InventoryItem> reloadedItems = productJsonService.readInventory(savedProductsFile, savedInventoryFile);
        List<ProductDto> expectedProductDtos = itemsToSave.stream()
                .map(InventoryItem::getProduct)
                .map(ProductMapper::toProductDto)
                .toList();
        List<ProductDto> reloadedProductDtos = reloadedItems.stream()
                .map(InventoryItem::getProduct)
                .map(ProductMapper::toProductDto)
                .toList();
        List<Integer> expectedQuantities = itemsToSave.stream()
                .map(InventoryItem::getQuantityInStock)
                .toList();
        List<Integer> reloadedQuantities = reloadedItems.stream()
                .map(InventoryItem::getQuantityInStock)
                .toList();

        assertAll(
                () -> assertEquals(2, savedProductDtos.size()),
                () -> assertEquals(2, reloadedItems.size()),
                () -> assertEquals(expectedProductDtos, savedProductDtos),
                () -> assertEquals(expectedProductDtos, reloadedProductDtos),
                () -> assertEquals(expectedQuantities, reloadedQuantities));
    }

    @Test void malformedJsonUsesTheServiceReadErrorBehavior() throws IOException {
        Path malformedProductsFile = writeTempFile("malformed-products.json", "[{ not valid JSON }");

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productJsonService.readProductDtos(malformedProductsFile));

        assertTrue(exception.getMessage().startsWith("Error reading JSON file:"));
    }

    @Test void missingProductFileUsesTheServiceReadErrorBehavior() {
        Path missingProductsFile = tempDirectory.resolve("missing-products.json");

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productJsonService.readProductDtos(missingProductsFile));

        assertTrue(exception.getMessage().startsWith("Error reading JSON file:"));
    }

    @Test void invalidDateInProductJsonRaisesDateTimeParseException() throws IOException {
        String invalidDateJson = """
                [{
                  "id": 31,
                  "name": "Notebook",
                  "description": "Lined notebook",
                  "dateOfManufacture": "03/14/2025",
                  "price": 3.49,
                  "category": "Office"
                }]
                """;
        Path invalidDateFile = writeTempFile("invalid-date-products.json", invalidDateJson);

        assertThrows(DateTimeParseException.class, () -> productJsonService.readProducts(invalidDateFile));
    }

    @Test void inventoryReferenceToMissingProductRaisesIllegalArgumentException() throws IOException {
        Path controlledProductsFile = writeTempFile("one-product.json", CONTROLLED_PRODUCTS_JSON);
        Path orphanInventoryFile = writeTempFile(
                "orphan-inventory.json", "[{\"id\": 999, \"quantityInStock\": 2}]");

        assertThrows(IllegalArgumentException.class,
                () -> productJsonService.readInventory(controlledProductsFile, orphanInventoryFile));
    }

    @Test void productionProductResourceLoadsExactlyOneHundredProductDtos() {
        Path productionProductsFile = Path.of("src", "main", "resources", "data", "products.json");

        assertEquals(100, productJsonService.readProductDtos(productionProductsFile).size());
    }

}
