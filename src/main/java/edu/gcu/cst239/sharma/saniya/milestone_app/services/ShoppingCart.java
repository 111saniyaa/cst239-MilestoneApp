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
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean checkout() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clearCart() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<CartItem> getAllCartItems() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllCartItems'");
    }
    
}
