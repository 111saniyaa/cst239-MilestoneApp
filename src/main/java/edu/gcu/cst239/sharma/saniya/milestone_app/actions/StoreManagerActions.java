package edu.gcu.cst239.sharma.saniya.milestone_app.actions;

import java.time.LocalDate;

import edu.gcu.cst239.sharma.saniya.milestone_app.services.StoreFront;
import edu.gcu.cst239.sharma.saniya.milestone_app.util.InputUtilities;  
import edu.gcu.cst239.sharma.saniya.milestone_app.models.Product;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.InventoryItem;

public class StoreManagerActions {

    StoreFront store;

    public StoreManagerActions(StoreFront store) {
        this.store = store;
    }

    public void handleManagerActions() {

        InputUtilities input = new InputUtilities();
        boolean exitRequested = false;
        while (!exitRequested) {
            System.out.println();
            System.out.println("Store Manager Menu:");
            System.out.println("1. View products in inventory");
            System.out.println("2. Search for product by name or description");
            System.out.println("3. Add product to inventory");
            System.out.println("4. Remove product from inventory");
            System.out.println("5. Update product details");
            System.out.println("6. Save inventory to file");
                System.out.println("7. Load inventory from file");
            System.out.println("0. Exit");
            int choice = input.readInt("Enter 1, 2, 3, 4, 5, 6, 7 or 0: ", 0, 7);
            switch (choice) {
                case 1:
                    System.out.println("You choose to view products in inventory.");
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
                    System.out.println("You choose to add a product to inventory.");


                    //Create product details

                    int productId = input.readInt("Enter the product ID: ");
                    String productName = input.readString("Enter the product name: ");
                    String productDescription = input.readString("Enter the product description: ");
                    String dateOfManufactureInput = input.readString("Enter the product date of manufacture (YYYY-MM-DD): ");
                    LocalDate dateOfManufacture = LocalDate.parse(dateOfManufactureInput);
                    double productPrice = input.readDouble("Enter the product price: ");
                    String productCategory = input.readString("Enter the product category: ");  

                    int quantityInStock = input.readInt("Enter the quantity in stock: ");

                    //Create product and inventory item
                    Product newProduct = Product.builder()
                            .id(productId)
                            .name(productName)
                            .description(productDescription)
                            .dateOfManufacture(dateOfManufacture)
                            .price(productPrice)
                            .category(productCategory)
                            .build();

                    InventoryItem newInventoryItem = new InventoryItem(newProduct, quantityInStock);
                    boolean added = store.getInventoryManager().addInventoryItem(newInventoryItem);

                    if(added) {
                        System.out.println("Product added to inventory successfully.");
                    } else {
                        System.out.println("Failed to add product. Product ID may already exist.");
                    }

                    break;

                case 4:
                    System.out.println("You choose to remove a product from inventory.");

                    int productIdToRemove = input.readInt("Enter the product ID to remove: ");
                    boolean removed = store.getInventoryManager().removeProductById(productIdToRemove);

                    if(removed) {
                        System.out.println("Product removed from inventory successfully.");
                    } else {
                        System.out.println("Failed to remove product. Product ID not found.");
                    }
                    break;

                case 5:
                    System.out.println("You choose to update product details in inventory.");

                    int productId2 = input.readInt("Enter the product ID: ");
                    String productName2 = input.readString("Enter the product name: ");
                    String productDescription2 = input.readString("Enter the product description: ");
                    String dateOfManufactureInput2 = input.readString("Enter the product date of manufacture (YYYY-MM-DD): ");
                    LocalDate dateOfManufacture2 = LocalDate.parse(dateOfManufactureInput2);
                    double productPrice2 = input.readDouble("Enter the product price: ");
                    String productCategory2 = input.readString("Enter the product category: ");  

                    //Create product and inventory item
                    Product newProduct2 = Product.builder()
                            .id(productId2)
                            .name(productName2)
                            .description(productDescription2)
                            .dateOfManufacture(dateOfManufacture2)
                            .price(productPrice2)
                            .category(productCategory2)
                            .build();

                    boolean updated = store.getInventoryManager().updateProduct(newProduct2);

                    if(updated) {
                        System.out.println("Product updated in inventory successfully.");
                    } else {
                        System.out.println("Failed to update product. Product ID not found.");
                    }

                    break;

                case 6:
                    System.out.println("You choose to save inventory to file.");
                    System.out.println("This is milestone #1. No actual fuctionality has been implemented yet.");  

                    break;
                case 7:
                    System.out.println("You choose to load inventory from file.");
                    System.out.println("This is milestone #1. No actual fuctionality has been implemented yet.");  
                    
                    break;
                case 0:
                    exitRequested = true;

                    break;
                default:
                    // This case should never occur because readInt enforces range
                    System.out.println("Invalid selection. Please try again.");
                }
            }

    }
    
}
