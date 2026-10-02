package edu.gcu.cst239.sharma.saniya.milestone_app.services;

/**
 * Represents the storefront application and provides access to its
 * major services.
 *
 * StoreFront does not directly store or manipulate inventory.
 * Inventory operations are delegated to InventoryManager.
 */
public class StoreFront {

    private final InventoryService inventoryManager;
    private final CartService cartManager;

    /**
     * Creates the storefront and its required services.
     */
    public StoreFront() {
        inventoryManager = new InventoryManager();
        cartManager = new ShoppingCart();
    }

    /**
     * Returns the service responsible for managing store inventory.
     *
     * @return the inventory manager
     */
    public InventoryService getInventoryManager() {
        return inventoryManager;
    }

    /**
     * Returns the service responsible for managing the shopping cart.
     *
     * @return the cart manager
     */
    public CartService getCartManager() {
        return cartManager;
    }
}
