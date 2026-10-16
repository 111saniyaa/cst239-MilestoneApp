package edu.gcu.cst239.sharma.saniya.milestone_app.services;

import edu.gcu.cst239.sharma.saniya.milestone_app.models.*;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryQueryTest {
    

    @Test 
    void returnsAllControlledInventoryItems(){
        List<InventoryItem> items = TestData.manager().getAllInventoryItems();
        assertEquals(List.of(1,2,3), TestData.ids(items));

        InventoryItem speaker = items.get(0);

        assertEquals("Speaker", speaker.getProduct().getName());
        assertEquals(10.0, speaker.getProduct().getPrice());
        assertEquals(5, speaker.getQuantityInStock());
        assertEquals("electronics", speaker.getProduct().getCategory());
    }

    @Test 
    void findAnExistingProductById(){
        assertEquals("Cable", TestData.manager().getInventoryItemByProductId(2).getProduct().getName());
    }

    @Test 
    void returnNullForUnknownProductId(){
        assertNull(TestData.manager().getInventoryItemByProductId(500));
    }

    @Test 
    void searchesItemsbyNameTest(){
        assertEquals(List.of(2), TestData.ids(TestData.manager().searchProductsByName("Cable")));
    }

    @Test 
    void searchesItemsbyDescriptionTest(){
        assertEquals(List.of(1,3), TestData.ids(TestData.manager().searchProductsByDescription("wireless")));
    }

    @Test 
    void testForCaseSensitivity(){
        assertEquals(List.of(2), TestData.ids(TestData.manager().searchProductsByName("caBlE")));
        assertEquals(List.of(1,3), TestData.ids(TestData.manager().searchProductsByDescription("WirEleSs")));
    }

    // what if name does not match
    @Test 
    void searchTermsDoesNotMatchForName(){
        assertTrue(TestData.manager().searchProductsByName("Earth").isEmpty());
    }

    // what if description does not match
    @Test
    void searchTermsDoesNotMatchForDescription(){
        assertTrue(TestData.manager().searchProductsByDescription("Earth").isEmpty());
    }


}
