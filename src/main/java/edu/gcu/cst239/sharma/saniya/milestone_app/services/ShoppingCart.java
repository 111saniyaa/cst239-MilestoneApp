package edu.gcu.cst239.sharma.saniya.milestone_app.services;

import java.util.List;

import edu.gcu.cst239.sharma.saniya.milestone_app.models.CartItem;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.Product;

public class ShoppingCart implements CartService {

    @Override
    public boolean addProduct(Product product, int quantity) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean updateQuantity(int productId, int quantity) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeProductFromCart(int productId) {
        // TODO Auto-generated method stub
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
