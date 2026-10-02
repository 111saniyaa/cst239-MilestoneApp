package edu.gcu.cst239.sharma.saniya.milestone_app.services;

import java.util.List;

import edu.gcu.cst239.sharma.saniya.milestone_app.models.Product;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.CartItem;

public interface CartService {
    /**
     * Returns a read-only copy of the current cart.
     * @return the current cart items
     */

    List<CartItem> getAllCartItems();

    /**
     * Adds a quantity when sufficient stock is available.
     * @param product the product to add
     * @param quantity the requested quantity
     * @return true when the cart changes
     */
    boolean addProduct(Product product, int quantity);

    /**
     * Changes the quantity of an existing cart item.
     * @param productId the product identifier
     * @param quantity the requested quantity
     * @return true when the cart changes
     */
    boolean updateQuantity(int productId, int quantity);

    /**
     * Removes the cart item associated with a product identifier.
     * @param productId the product identifier
     * @return true when an item is removed
     */
    boolean removeProductFromCart(int productId);

    /**
     * Calculates the current cart total.
     * @return the total price of all cart items
     */
    double getCartTotal();

    /**
     * Completes a valid purchase, updates stock, and clears the cart.
     * @return true when checkout succeeds
     */
    boolean checkout();

    /** Removes every item from the cart. */
    void clearCart();

    // void displayCartItems();

    
}
