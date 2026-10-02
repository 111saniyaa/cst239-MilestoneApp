package edu.gcu.cst239.sharma.saniya.milestone_app.services;

import java.util.List;
import java.util.ArrayList;

import edu.gcu.cst239.sharma.saniya.milestone_app.models.CartItem;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.Product;

public class ShoppingCart implements CartService {

    private final List<CartItem> cartItems;

    public ShoppingCart() {
        cartItems = new ArrayList<>();
    }



    @Override
    public boolean addProduct(Product product, int quantity) {
        // TODO Auto-generated method stub

        boolean result = cartItems.add(new CartItem(product, quantity));
        if (result) {
            System.out.println("Product added to cart: " + product.getName() + ", Quantity: " + quantity);
            return true;
        } else {
            System.out.println("Failed to add product to cart: " + product.getName());
        }
        return false;
    }

    @Override
    public boolean updateQuantity(int productId, int quantity) {
        
        // first we will check if the product exists in the cart
        for (CartItem item : cartItems) {
            if(item.getProduct().getId() == productId) {
                item.setQuantity(quantity);
                System.out.println("Updated quantity for product: " + item.getProduct().getName() + " to " + quantity);
                return true;
            }
        }

        System.out.println("Product with ID " + productId + " not found in cart.");
        return false;
    }

    @Override
    public boolean removeProductFromCart(int productId) {

        for (CartItem item : cartItems) {
            if(item.getProduct().getId() == productId) {
                cartItems.remove(item);
                System.out.println("Removed product from cart: " + item.getProduct().getName());
                return true;
            }
        }
        System.out.println("Product with ID " + productId + " not found in cart.");
        return false;
    }

    @Override
    public double getCartTotal() {

        double total = 0.0;

        for (CartItem item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }

        return total;
    }

    @Override
    public boolean checkout() {
        if (cartItems.isEmpty()) {
            System.out.println("Cart is empty. Cannot proceed to checkout.");
            return false;
        }
        
        System.out.println("Checkout complete. Cart is now empty.");
        return true;
    }

    @Override
    public void clearCart() {
        // TODO Auto-generated method stub
        cartItems.clear();
        System.out.println("Cart cleared.");
        
    }

    @Override
    public List<CartItem> getAllCartItems() {
        return List.copyOf(cartItems);
    }

    // @Override 
    // public void displayCartItems() {
    //     if (cartItems.isEmpty()) {
    //         System.out.println("Your cart is empty.");
    //         return;
    //     }

    //     System.out.println("Current items in your cart:");
    //     for (CartItem item : cartItems) {
    //         System.out.println("Product: " + item.getProduct().getName() + ", Quantity: " + item.getQuantity() + ", Price: $" + item.getProduct().getPrice());
    //     }
    // }
    
}
