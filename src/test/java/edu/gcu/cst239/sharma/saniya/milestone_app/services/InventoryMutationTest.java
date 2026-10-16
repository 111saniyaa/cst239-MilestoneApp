package edu.gcu.cst239.sharma.saniya.milestone_app.services;


import edu.gcu.cst239.sharma.saniya.milestone_app.models.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.time.LocalDate;

public class InventoryMutationTest {
    
    @Test
    void addNewInventoryItemsTest(){
        InventoryManager manager = TestData.manager();

        InventoryItem newTablet = TestData.inventoryItem(4, "ipad pro", "The best tablet ever - only for rich people",
                                    LocalDate.of(2026, 2,1), 999.99, 40, "electronics");

        assertTrue(manager.addInventoryItem(newTablet));

        assertEquals("ipad pro", newTablet.getProduct().getName());
        assertEquals(999.99, newTablet.getProduct().getPrice());
        assertEquals(40, newTablet.getQuantityInStock());
        assertEquals("electronics", newTablet.getProduct().getCategory());
    }

    @Test 
    void addingDuplicateProductIdShouldReject(){

        InventoryManager manager = TestData.manager();

        InventoryItem newTablet = TestData.inventoryItem(2, "ipad pro", "The best tablet ever - only for rich people",
                                    LocalDate.of(2026, 2,1), 999.99, 40, "electronics");
        
        assertFalse(manager.addInventoryItem(newTablet));
    }

    @Test 
    void updatingExistingProductShouldPreserveQuantity(){
        InventoryManager manager = TestData.manager();

        Product newTablet = TestData.product(2, "ipad pro", "The best tablet ever - only for rich people",
                                    LocalDate.of(2026, 2,1), 999.99, "electronics");

        assertTrue(manager.updateProduct(newTablet));
        
        assertEquals(10, manager.getInventoryItemByProductId(2).getQuantityInStock());
    }


    @Test 
    void updatingUnknownProductIdShouldFail(){
        InventoryManager manager = TestData.manager();

        Product unkonwn = TestData.product(500, "Lizard Food", "Very yummy, keeps lizards in your room, very welcoming.", 
            LocalDate.of(2026,10,12), 1.00, "pets"); 
        
        assertFalse(manager.updateProduct(unkonwn));

    }

    @Test 
    void removeExistingItemsShouldRemoveId(){
        InventoryManager manager = TestData.manager();
        assertTrue(manager.removeProductById(2));

        assertEquals(List.of(1,3), TestData.ids(manager.getAllInventoryItems()));
    }

    @Test 
    void removeUnknownItemsShouldRemoveId(){
        InventoryManager manager = TestData.manager();
        assertFalse(manager.removeProductById(500));

        assertEquals(List.of(1,2,3), TestData.ids(manager.getAllInventoryItems()));
    }

}
