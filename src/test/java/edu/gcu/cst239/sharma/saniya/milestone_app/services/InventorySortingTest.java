package edu.gcu.cst239.sharma.saniya.milestone_app.services;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.time.LocalDate;

public class InventorySortingTest {
    
    @Test 
    void sortByProductNameAtoZ(){
        List<InventoryItem> sorted = TestData.manager().getInventoryItemsSortedByName();
        assertEquals(List.of(2,3,1),  TestData.ids(sorted));
    }

    @Test 
    void sortByProductPrice(){
        List<InventoryItem> sorted = TestData.manager().getInventoryItemsSortedByPrice();
        assertEquals(List.of(1,2,3),  TestData.ids(sorted));
    }

    @Test 
    void sortByDateOfManufacture(){
        List<InventoryItem> sorted = TestData.manager().getInventoryItemsSortedByDate();
        assertEquals(List.of(1,2,3),     TestData.ids(sorted));

    }

    
    @Test 
    void verifySortingisNotChangingActualInventory(){
        InventoryManager manager = TestData.manager();
        List<InventoryItem> sorted = manager.getInventoryItemsSortedByName();
        List<InventoryItem> actual = manager.getAllInventoryItems();

        assertEquals(List.of(2,3,1),  TestData.ids(sorted));
        assertEquals(List.of(1,2,3), TestData.ids(actual));
    }
}
