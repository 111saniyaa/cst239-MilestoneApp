package edu.gcu.cst239.sharma.saniya.milestone_app.actions;

import edu.gcu.cst239.sharma.saniya.milestone_app.util.InputUtilities;
import edu.gcu.cst239.sharma.saniya.milestone_app.services.StoreFront;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.Product;

import java.util.List;

import edu.gcu.cst239.sharma.saniya.milestone_app.models.CartItem;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.InventoryItem;

public class CustomerActions {

    StoreFront store;

    public CustomerActions(StoreFront store) {
        this.store = store;
    }

    public void handleCustomerActions() {
        // Implement customer actions here

        InputUtilities input = new InputUtilities();
        boolean exitRequested = false;
        while (!exitRequested) {
            System.out.println();
            System.out.println("Customer Menu:");
            System.out.println("1. View Products");
            System.out.println("2. Search for product by name or description");
            System.out.println("3. Add product to cart");
            System.out.println("4. Remove product from cart");
            System.out.println("5. View cart");
            System.out.println("6. Checkout");
            System.out.println("0. Exit");
            int choice = input.readInt("Enter 1, 2, 3, 4, 5, 6 or 0: ", 0, 6);
            switch (choice) {
                case 1:
    System.out.println("You choose to view products.");

    System.out.println("Sort products by:");
    System.out.println("1. Name");
    System.out.println("2. Price");
    System.out.println("3. Date");

    int sortChoice = input.readInt("Enter 1, 2, or 3: ", 1, 3);

    List<InventoryItem> products;

    if (sortChoice == 1) {
        products = store.getInventoryManager().getInventoryItemsSortedByName();
    } 
    else if (sortChoice == 2) {
        products = store.getInventoryManager().getInventoryItemsSortedByPrice();
    } 
    else {
        products = store.getInventoryManager().getInventoryItemsSortedByDate();
    }

    products.forEach(item -> {
        System.out.println("Product ID: " + item.getProduct().getId());
        System.out.println("Product Name: " + item.getProduct().getName());
        System.out.println("Product Description: " + item.getProduct().getDescription());
        System.out.println("Product Date of Manufacture: " + item.getProduct().getDateOfManufacture());
        System.out.println("Product Price: $" + item.getProduct().getPrice());
        System.out.println("Product Category: " + item.getProduct().getCategory());
        System.out.println("Quantity in Stock: " + item.getQuantityInStock());
        System.out.println();
    });

    break;
                case 2:
                    System.out.println("Do you want to search by name or description?");

                    String searchTerm = input.readString("Enter the 'name' or 'description' to search for: ");

                    if(searchTerm.equalsIgnoreCase("name")) {
                        System.out.println("You choose to search for a product by name.");
                        String nameToSearch = input.readString("Enter the product name to search for: ");
                        store.getInventoryManager().searchProductsByName(nameToSearch).forEach(item -> {
                            System.out.println("Product ID: " + item.getProduct().getId());
                            System.out.println("Product Name: " + item.getProduct().getName());
                            System.out.println("Product Description: " + item.getProduct().getDescription());
                            System.out.println("Product Date of Manufacture: " + item.getProduct().getDateOfManufacture());
                            System.out.println("Product Price: $" + item.getProduct().getPrice());
                            System.out.println("Product Category: " + item.getProduct().getCategory());
                            System.out.println("Quantity in Stock: " + item.getQuantityInStock());
                            System.out.println();
                        });
                        
                    } else if(searchTerm.equalsIgnoreCase("description")) {
                        System.out.println("You choose to search for a product by description.");
                        String descriptionToSearch = input.readString("Enter the product description to search for: ");
                        store.getInventoryManager().searchProductsByDescription(descriptionToSearch).forEach(item -> {
                            System.out.println("Product ID: " + item.getProduct().getId());
                            System.out.println("Product Name: " + item.getProduct().getName());
                            System.out.println("Product Description: " + item.getProduct().getDescription());
                            System.out.println("Product Date of Manufacture: " + item.getProduct().getDateOfManufacture());
                            System.out.println("Product Price: $" + item.getProduct().getPrice());
                            System.out.println("Product Category: " + item.getProduct().getCategory());
                            System.out.println("Quantity in Stock: " + item.getQuantityInStock());
                            System.out.println();
                        });
                    } else {
                        System.out.println("Invalid search term. Please enter 'name' or 'description'.");
                    }
                    break;

                case 3:
                    System.out.println("You choose to add a product to the cart.");
                    // input product ID and quantity
                    int productIdToAdd = input.readInt("Enter the product ID to add to the cart: ");
                    int quantityToAdd = input.readInt("Enter the quantity to add to the cart: ");


                    InventoryItem inventoryItem = store.getInventoryManager().getInventoryItemByProductId(productIdToAdd);
                    
                    if(inventoryItem == null) {
                        System.out.println("Product with ID " + productIdToAdd + " not found in inventory.");
                        break;
                    }
                    
                    Product productToAdd = store.getInventoryManager().getInventoryItemByProductId(productIdToAdd).getProduct();
                    

                    if(inventoryItem.getQuantityInStock() < quantityToAdd) {
                        System.out.println("Not enough stock available. Available quantity: " + inventoryItem.getQuantityInStock());
                        break;
                    }

                    if(quantityToAdd <= 0) {
                        System.out.println("Quantity must be greater than zero.");
                        break;
                    }

                    List<CartItem> currentCartItems = store.getCartManager().getAllCartItems();

                    boolean addOrUpdate = true;
                    for(CartItem item : currentCartItems) {
                        if(item.getProduct().getId() == productIdToAdd) {
                            System.out.println("Product with ID " + productIdToAdd + " is already in the cart. Updating quantity.");
                            store.getCartManager().updateQuantity(productIdToAdd, item.getQuantity() + quantityToAdd);
                            addOrUpdate = false;
                            break;
                        }
                    }

                    if(addOrUpdate) {
                        store.getCartManager().addProduct(productToAdd, quantityToAdd);
                    }

                    break;

                case 4: 
                    System.out.println("You choose to remove a product from the cart.");

                    int productIdToRemove = input.readInt("Enter the product ID to remove from the cart: ");
                    store.getCartManager().removeProductFromCart(productIdToRemove);
                    break;

                case 5:
    System.out.println("You choose to view the cart.");

    store.getCartManager().getAllCartItems().forEach(item -> {
        System.out.println("Product ID: " + item.getProduct().getId());
        System.out.println("Product Name: " + item.getProduct().getName());
        System.out.println("Product Description: " + item.getProduct().getDescription());
        System.out.println("Product Price: $" + item.getProduct().getPrice());
        System.out.println("Quantity in Cart: " + item.getQuantity());
        System.out.println("Subtotal: $" + 
            (item.getProduct().getPrice() * item.getQuantity()));
        System.out.println();
    });

    System.out.println("Total Cart Value: $" + 
        store.getCartManager().getCartTotal());

    if (!store.getCartManager().getAllCartItems().isEmpty()) {

        String updateChoice = input.readString(
            "Would you like to change a quantity? Enter yes or no: "
        );

        if (updateChoice.equalsIgnoreCase("yes")) {

            int productId = input.readInt(
                "Enter the product ID you want to update: "
            );

            int newQuantity = input.readInt(
                "Enter the new quantity: "
            );


            boolean updated = store.getCartManager()
                    .updateQuantity(productId, newQuantity);

            if (updated) {
                System.out.println("Quantity updated successfully.");
            } else {
                System.out.println("Quantity could not be updated.");
            }
        }
    }

    break;
                case 6:
                    System.out.println("You choose to checkout.");
                    
                    if(store.getCartManager().checkout()) {
                        System.out.println("Checkout successful. Thank you for your purchase!");

                        List<CartItem> cartItems = store.getCartManager().getAllCartItems();

                        for (CartItem item : cartItems) {

                            int productId = item.getProduct().getId();
                            int cartQuantityToremove = item.getQuantity();

                            int inventoryQuatityInStock = store.getInventoryManager().getInventoryItemByProductId(productId).getQuantityInStock();

                            
                            InventoryItem currentInventoryItem = store.getInventoryManager().getInventoryItemByProductId(productId);

                            if(currentInventoryItem == null) {
                                System.out.println("Product with ID " + productId + " not found in inventory.");
                                System.out.println("Checkout failed. Please try again.");
                                break;
                            }

                            if(currentInventoryItem.getQuantityInStock() < cartQuantityToremove) {
                                System.out.println("Not enough stock available for Product ID: " + productId + ". Available quantity: " + currentInventoryItem.getQuantityInStock());
                                System.out.println("Checkout failed. Please try again.");
                                break;
                            }

                            store.getInventoryManager().updateQuantity(productId, inventoryQuatityInStock - cartQuantityToremove);
                        }

                        store.getCartManager().clearCart();
                        System.out.println("Checkout complete. Cart is now empty.");

                    } else {
                        System.out.println("Checkout failed. Please try again.");
                    }

                    break;

                case 0:
                    exitRequested = true;
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

    }
    
}
