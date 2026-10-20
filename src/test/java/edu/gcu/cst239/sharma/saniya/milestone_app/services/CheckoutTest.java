package edu.gcu.cst239.sharma.saniya.milestone_app.services;

import edu.gcu.cst239.sharma.saniya.milestone_app.services.ShoppingCart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/** Tests coordinated checkout behavior using controlled in-memory data. */
class CheckoutTest {

    @Test
    void successfulCheckoutReducesInventoryByPurchasedQuantities() {
        InventoryService inventory = TestData.manager();
        CartService cart = new ShoppingCart(inventory);
        assertTrue(cart.addProduct(inventory.getInventoryItemByProductId(1).getProduct(), 2));
        assertTrue(cart.addProduct(inventory.getInventoryItemByProductId(2).getProduct(), 3));

        assertTrue(cart.checkout());

        assertEquals(3, inventory.getInventoryItemByProductId(1).getQuantityInStock());  
        assertEquals(7, inventory.getInventoryItemByProductId(2).getQuantityInStock()); 
        assertEquals(15, inventory.getInventoryItemByProductId(3).getQuantityInStock()); 
    }

    @Test
    void successfulCheckoutClearsTheCart() {
        InventoryService inventory = TestData.manager();
        CartService cart = new ShoppingCart(inventory);
        assertTrue(cart.addProduct(inventory.getInventoryItemByProductId(1).getProduct(), 1));

        assertTrue(cart.checkout());

        assertTrue(cart.getAllCartItems().isEmpty());
        assertEquals(0.0, cart.getCartTotal(), 0.0001);
    }

    @Test
    void insufficientInventoryPreventsPurchase() {
        InventoryService inventory = TestData.manager();
        CartService cart = new ShoppingCart(inventory);
        assertTrue(cart.addProduct(inventory.getInventoryItemByProductId(3).getProduct(), 99));

        assertFalse(cart.checkout());

        assertEquals(15, inventory.getInventoryItemByProductId(3).getQuantityInStock()); // unchanged
    }


    @Test
    void failedCheckoutDoesNotPartiallyReduceInventory() {
        InventoryService inventory = TestData.manager();
        CartService cart = new ShoppingCart(inventory);
        assertTrue(cart.addProduct(inventory.getInventoryItemByProductId(1).getProduct(), 2)); // stock 10, sufficient
        assertTrue(cart.addProduct(inventory.getInventoryItemByProductId(3).getProduct(), 26)); // stock 5, insufficient

        assertFalse(cart.checkout());

        assertEquals(5, inventory.getInventoryItemByProductId(1).getQuantityInStock()); // still 10, not 8
        assertEquals(10, inventory.getInventoryItemByProductId(2).getQuantityInStock()); // unchanged
        assertEquals(15, inventory.getInventoryItemByProductId(3).getQuantityInStock());  // unchanged
    }

    @Test
    void emptyCartCheckoutReturnsFalse() {
        InventoryService inventory = TestData.manager();
        CartService cart = new ShoppingCart(inventory);

        assertFalse(cart.checkout());

        assertTrue(cart.getAllCartItems().isEmpty());
        assertEquals(5, inventory.getInventoryItemByProductId(1).getQuantityInStock());
        assertEquals(10, inventory.getInventoryItemByProductId(2).getQuantityInStock());
        assertEquals(15, inventory.getInventoryItemByProductId(3).getQuantityInStock());
    }

}
