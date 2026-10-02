package edu.gcu.cst239.sharma.saniya.milestone_app.actions;

import edu.gcu.cst239.sharma.saniya.milestone_app.util.InputUtilities;
import edu.gcu.cst239.sharma.saniya.milestone_app.services.StoreFront;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.Product;
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

                    /*
                    store.getInventoryManager().getAllInventoryItems().forEach(item -> {
                        System.out.println(item);
                    });
                    */

                    store.getInventoryManager().getAllInventoryItems().forEach(item -> {
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

                    Product productToAdd = store.getInventoryManager().getInventoryItemByProductId(productIdToAdd).getProduct();
                    
                    InventoryItem inventoryItem = store.getInventoryManager().getInventoryItemByProductId(productIdToAdd);

                    if(inventoryItem == null) {
                        System.out.println("Product with ID " + productIdToAdd + " not found in inventory.");
                        break;
                    }

                    if(inventoryItem.getQuantityInStock() < quantityToAdd) {
                        System.out.println("Not enough stock available. Available quantity: " + inventoryItem.getQuantityInStock());
                        break;
                    }

                    if(quantityToAdd <= 0) {
                        System.out.println("Quantity must be greater than zero.");
                        break;
                    }

                    store.getCartManager().addProduct(productToAdd, quantityToAdd);

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
                        System.out.println("Product Date of Manufacture: " + item.getProduct().getDateOfManufacture());
                        System.out.println("Product Price: $" + item.getProduct().getPrice());
                        System.out.println("Product Category: " + item.getProduct().getCategory());
                        System.out.println("Quantity in Cart: " + item.getQuantity());
                        System.out.println("Subtotal for this item: $" + (item.getProduct().getPrice() * item.getQuantity()));
                        System.out.println();
                    });

                    System.out.println("Total Cart Value: $" + store.getCartManager().getCartTotal());
                    break;

                case 6:
                    System.out.println("You choose to checkout.");
                    System.out.println("This is milestone #1. No actual fuctionality has been implemented yet.");   

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
