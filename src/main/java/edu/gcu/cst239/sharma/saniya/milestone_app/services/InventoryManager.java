package edu.gcu.cst239.sharma.saniya.milestone_app.services;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Comparator; 

import edu.gcu.cst239.sharma.saniya.milestone_app.models.InventoryItem;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.Product;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.ProductDto;

public class InventoryManager implements InventoryService{

    private final List<InventoryItem> inventory;   


    // Milestone 4: Loading Data from JSON Files
    ProductJsonService productJsonService = new ProductJsonService();

    Path productsFilePath = Path.of("src", "main", "resources", "data", "products.json");
    Path inventoryFilePath = Path.of("src", "main", "resources", "data", "inventory.json");

    public InventoryManager() {
        inventory = new ArrayList<>();
        initializeInventory();
    }

    // 

    public InventoryManager(Collection<InventoryItem> items) {
        
        Objects.requireNonNull(items, "Inventory items collection cannot be null");
        inventory = new ArrayList<>(items);

        for(InventoryItem item : inventory) {
            if(item == null || item.getProduct() == null) {
                throw new IllegalArgumentException("Inventory items and their products cannot be null");
            }
        }
    }



    private void initializeInventory(){
        readInventoryFromJson();
    }



    @Override
    public void readInventoryFromJson() {
        List<InventoryItem> loadedInventory = productJsonService.readInventory(productsFilePath, inventoryFilePath);
        // Clear the current inventory and add all loaded items
        inventory.clear();

        // Add all loaded items to the inventory
        inventory.addAll(loadedInventory);
    }

    @Override 
    public void saveInventoryToJson() {
        try{
            productJsonService.writeInventoryItems(productsFilePath, inventoryFilePath, inventory);
        }catch(Exception e){
            System.err.println("Error saving inventory to JSON: " + e.getMessage());
        }
    }



    @Override
    public List<InventoryItem> getAllInventoryItems() {
        
        return List.copyOf(inventory);
    }


    @Override
    public InventoryItem getInventoryItemByProductId(int productId) {

        for(InventoryItem item : inventory) {
            if(item.getProduct().getId() == productId) {
                return item;
            }
        }
        return null;
    }

    @Override
    public List<InventoryItem> searchProductsByName(String searchTerm) {
        

        String normalizedTerm = searchTerm.trim().toLowerCase(Locale.ROOT);

        List<InventoryItem> matches = new ArrayList<>();

        for(InventoryItem item : inventory) {
            String productName = item.getProduct().getName().toLowerCase(Locale.ROOT);

            if(productName.contains(normalizedTerm)) {
                matches.add(item);
            }
        }

        return matches;
        
    }

    @Override
    public List<InventoryItem> searchProductsByDescription(String searchTerm) {
        
        String normalizedTerm = searchTerm.trim().toLowerCase(Locale.ROOT);

        List<InventoryItem> matches = new ArrayList<>();

        for(InventoryItem item : inventory) {
            String productDescription = item.getProduct().getDescription().toLowerCase(Locale.ROOT);

            if(productDescription.contains(normalizedTerm)) {
                matches.add(item);
            }
        }

        return matches;
    }

    @Override
    public boolean addInventoryItem(InventoryItem item) {
        
        if (item == null || item.getProduct() == null) {
            return false;
        }

        if(getInventoryItemByProductId(item.getProduct().getId()) != null) {
            return false;
        }

        
        return inventory.add(item);
    }

    @Override
    public boolean updateProduct(Product updatedProduct) {
        

        for(int index = 0; index < inventory.size(); index++) {
            InventoryItem currentItem = inventory.get(index);

            if(currentItem.getProduct().getId() == updatedProduct.getId()){

                InventoryItem updatedItem = new InventoryItem(updatedProduct, currentItem.getQuantityInStock());
                
                inventory.set(index, updatedItem);
                
                return true;    
            }
        }
        return false;
    }



    @Override
    public boolean updateQuantity(int productId, int quantityInStock) {
        

        //Product product = getInventoryItemByProductId(productId).getProduct();

        for(int index = 0; index < inventory.size();index ++){
            InventoryItem currentItem = inventory.get(index);

            if(currentItem.getProduct().getId() == productId ){

                InventoryItem updatedItem = new InventoryItem(currentItem.getProduct(), quantityInStock);
                inventory.set(index, updatedItem);
                return true;    
            }
        }

        return false;
    }

    @Override
    public boolean removeProductById(int productId) {
        

        InventoryItem item = getInventoryItemByProductId(productId);

        if(item == null) {
            return false;
        }
        return inventory.remove(item);        
    }

    @Override
    public void clearInventory() {
        
        inventory.clear();
    }


    @Override 
public List<InventoryItem> getInventoryItemsSortedByName(){

    List<InventoryItem> sortedList = new ArrayList<>(inventory);

    sortedList.sort(
        Comparator.comparing(item -> item.getProduct().getName())
    );

    return sortedList;
}

@Override
public List<InventoryItem> getInventoryItemsSortedByPrice(){

    List<InventoryItem> sortedList = new ArrayList<>(inventory);

    sortedList.sort(
        Comparator.comparingDouble(item -> item.getProduct().getPrice())
    );

    return sortedList;
}

@Override 
public List<InventoryItem> getInventoryItemsSortedByDate(){

    List<InventoryItem> sortedList = new ArrayList<>(inventory);

    sortedList.sort(
        Comparator.comparing(item -> item.getProduct().getDateOfManufacture())
    );

    return sortedList;
}
}
