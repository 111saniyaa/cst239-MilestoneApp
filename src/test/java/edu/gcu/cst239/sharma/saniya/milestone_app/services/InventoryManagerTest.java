package edu.gcu.cst239.sharma.saniya.milestone_app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.gcu.cst239.sharma.saniya.milestone_app.models.InventoryItem;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.Product;

/** Tests inventory service behavior with small, in-memory test data. */
class InventoryManagerTest {

    @Test
    void addsNewProductToInventory() {
        InventoryService inventory = TestData.manager();
        InventoryItem item = TestData.inventoryItem(
                4, "Travel Mug", "Insulated travel mug", LocalDate.of(2025, 4, 10), 18.99, 6, "Kitchen");

        assertTrue(inventory.addInventoryItem(item));

        InventoryItem added = inventory.getInventoryItemByProductId(4);
        assertEquals("Travel Mug", added.getProduct().getName());
        assertEquals(6, added.getQuantityInStock());
        assertEquals(4, inventory.getAllInventoryItems().size());
    }

    @Test
    void rejectsAddingProductWithDuplicateId() {
        InventoryService inventory = TestData.manager();
        InventoryItem duplicate = TestData.inventoryItem(
                1, "Replacement Speaker", "Duplicate identifier", LocalDate.of(2025, 4, 10), 25.00, 2, "Audio");

        assertFalse(inventory.addInventoryItem(duplicate));

        assertEquals(3, inventory.getAllInventoryItems().size());
        assertEquals("Speaker", inventory.getInventoryItemByProductId(1).getProduct().getName());
    }

    @Test
    void findsProductByIdAndReturnsNullWhenMissing() {
        InventoryService inventory = TestData.manager();

        assertEquals("Cable", inventory.getInventoryItemByProductId(2).getProduct().getName());
        assertNull(inventory.getInventoryItemByProductId(99));
    }

    @Test
    void searchesProductNamesByCaseInsensitivePartialMatch() {
        InventoryService inventory = TestData.manager();

        List<InventoryItem> matches = inventory.searchProductsByName("  sPeA  ");

        assertEquals(List.of(1), TestData.ids(matches));
    }

    @Test
    void searchesProductDescriptionsByCaseInsensitivePartialMatch() {
        InventoryService inventory = TestData.manager();

        List<InventoryItem> matches = inventory.searchProductsByDescription("NOISE-CANCELLING");

        assertEquals(List.of(3), TestData.ids(matches));
    }

    @Test
    void updatesProductDetailsAndPreservesExistingStock() {
        InventoryService inventory = TestData.manager();
        Product updatedProduct = TestData.product(
                1, "Studio Speaker", "Updated Bluetooth speaker", LocalDate.of(2025, 5, 1), 45.00, "Audio");

        assertTrue(inventory.updateProduct(updatedProduct));

        InventoryItem updated = inventory.getInventoryItemByProductId(1);
        assertEquals("Studio Speaker", updated.getProduct().getName());
        assertEquals("Updated Bluetooth speaker", updated.getProduct().getDescription());
        assertEquals(LocalDate.of(2025, 5, 1), updated.getProduct().getDateOfManufacture());
        assertEquals(45.00, updated.getProduct().getPrice(), 0.0001);
        assertEquals("Audio", updated.getProduct().getCategory());
        assertEquals(5, updated.getQuantityInStock());
    }

    @Test
    void returnsFalseWhenUpdatingMissingProduct() {
        InventoryService inventory = TestData.manager();
        Product missingProduct = TestData.product(
                99, "Unknown Product", "Not in inventory", LocalDate.of(2025, 5, 1), 1.00, "Other");

        assertFalse(inventory.updateProduct(missingProduct));
    }

    @Test
    void removesProductById() {
        InventoryService inventory = TestData.manager();

        assertTrue(inventory.removeProductById(2));

        assertNull(inventory.getInventoryItemByProductId(2));
        assertEquals(2, inventory.getAllInventoryItems().size());
    }

    @Test
    void returnsFalseWhenRemovingMissingProduct() {
        InventoryService inventory = TestData.manager();

        assertFalse(inventory.removeProductById(99));

        assertEquals(3, inventory.getAllInventoryItems().size());
    }
}
