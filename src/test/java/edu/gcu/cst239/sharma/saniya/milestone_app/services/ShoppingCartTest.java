package edu.gcu.cst239.sharma.saniya.milestone_app.services;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import edu.gcu.cst239.sharma.saniya.milestone_app.models.CartItem;

/** Tests shopping-cart behavior through CartService using controlled in-memory data. */
class ShoppingCartTest {
    

    @Test
    void addProductAddsANewLine() {
        CartService cart = new ShoppingCart();

        assertTrue(cart.addProduct(TestData.manager().getInventoryItemByProductId(1).getProduct(), 2));

        assertEquals(1, cart.getAllCartItems().size());
        assertEquals(2, cart.getAllCartItems().get(0).getQuantity());
        assertEquals(20.00, cart.getCartTotal());
    }

    @Test
    void addProductForExistingProductIncreasesQuantity() {
        CartService cart = new ShoppingCart();
        assertTrue(cart.addProduct(TestData.manager().getInventoryItemByProductId(1).getProduct(), 1));
        assertTrue(cart.addProduct(TestData.manager().getInventoryItemByProductId(1).getProduct(), 2));
        List<CartItem> items = cart.getAllCartItems();
        assertEquals(1, items.size());
        assertEquals(3, items.get(0).getQuantity());
        assertEquals(30.00, cart.getCartTotal()); // 10.00 x 3  
    }

    @Test
    void addProductRejectsZeroAndNegativeQuantities() {
        CartService cart = new ShoppingCart();
        assertFalse(cart.addProduct(TestData.manager().getInventoryItemByProductId(1).getProduct(), 0));
        assertFalse(cart.addProduct(TestData.manager().getInventoryItemByProductId(1).getProduct(), -2));
        assertEquals(0, cart.getAllCartItems().size());
        assertEquals(0.0, cart.getCartTotal());
    }

    @Test
    void addProductRejectsNullProduct() {
        CartService cart = new ShoppingCart();
        assertFalse(cart.addProduct(null, 2));
        assertEquals(0, cart.getAllCartItems().size());
    }

    @Test
    void updateQuantityChangesAnExistingLine() {
        CartService cart = new ShoppingCart();
        assertTrue(cart.addProduct(TestData.manager().getInventoryItemByProductId(1).getProduct(), 2));
        assertTrue(cart.updateQuantity(1, 5));
        List<CartItem> items = cart.getAllCartItems();
        assertEquals(5, items.get(0).getQuantity());
        assertEquals(50.00, cart.getCartTotal()); // 10.00 x 5
    }

    @Test
    void updateQuantityRejectsZeroAndNegativeQuantities() {
        CartService cart = new ShoppingCart();
        assertTrue(cart.addProduct(TestData.manager().getInventoryItemByProductId(1).getProduct(), 2));
        assertFalse(cart.updateQuantity(1, 0));
        assertFalse(cart.updateQuantity(1, -1));
        assertEquals(2, cart.getAllCartItems().get(0).getQuantity());
    }

    @Test
    void updateQuantityForUnknownProductFails() {
        CartService cart = new ShoppingCart();
        assertFalse(cart.updateQuantity(99, 3));
        assertEquals(0, cart.getAllCartItems().size());
    }

    @Test
    void removeProductRemovesTheLine() {
        CartService cart = new ShoppingCart();
        assertTrue(cart.addProduct(TestData.manager().getInventoryItemByProductId(1).getProduct(), 1));
        assertTrue(cart.addProduct(TestData.manager().getInventoryItemByProductId(3).getProduct(), 2));
        assertTrue(cart.removeProductFromCart(1));
        List<CartItem> items = cart.getAllCartItems();
        assertEquals(1, items.size());
        assertEquals(3, items.get(0).getProduct().getId());
        assertEquals(2, items.get(0).getQuantity());
        assertEquals(60.00, cart.getCartTotal()); // 
    }

    @Test
    void cartTotalSumsPriceTimesQuantity() {
        CartService cart = new ShoppingCart();
        assertTrue(cart.addProduct(TestData.manager().getInventoryItemByProductId(1).getProduct(), 2));
        assertTrue(cart.addProduct(TestData.manager().getInventoryItemByProductId(2).getProduct(), 3));
        assertEquals(80.00, cart.getCartTotal()); 
        assertTrue(cart.updateQuantity(1, 5));
        assertEquals(110.00, cart.getCartTotal()); 
    }

    @Test
    void emptyCartReturnsExpectedState() {
        CartService cart = new ShoppingCart();
        List<CartItem> items = cart.getAllCartItems();
        assertTrue(items.isEmpty());
        assertEquals(0.0, cart.getCartTotal());
    }
}

