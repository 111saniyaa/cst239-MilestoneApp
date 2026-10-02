package edu.gcu.cst239.sharma.saniya.milestone_app.services;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import edu.gcu.cst239.sharma.saniya.milestone_app.models.InventoryItem;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.Product;

public class InventoryManager implements InventoryService{

    private final List<InventoryItem> inventory;   

    public InventoryManager() {
        inventory = new ArrayList<>();
        initializeInventory();
    }



    private void initializeInventory(){

        Product laptop = Product.builder()
                .id(1)
                .name("Lenovo IdeaPad 5")
                .description("15-inch laptop with 16GB RAM and 512GB SSD")
                .dateOfManufacture(LocalDate.of(2026, 8, 10))
                .price(699.99)
                .category("electronics")
                .build();

        Product headphones = Product.builder()
                .id(2)
                .name("Sony WH-1000XM6")
                .description("Wireless noise-canceling over-ear headphones")
                .dateOfManufacture(LocalDate.of(2026, 7, 22))
                .price(399.99)
                .category("electronics")
                .build();

        Product coffeeMaker = Product.builder()
                .id(3)
                .name("Breville Coffee Maker")
                .description("Programmable 12-cup drip coffee maker")
                .dateOfManufacture(LocalDate.of(2026, 6, 18))
                .price(149.99)
                .category("appliances")
                .build();

        Product runningShoes = Product.builder()
                .id(4)
                .name("Nike Air Zoom Pegasus")
                .description("Lightweight running shoes with responsive cushioning")
                .dateOfManufacture(LocalDate.of(2026, 5, 30))
                .price(129.99)
                .category("footwear")
                .build();

        Product backpack = Product.builder()
                .id(5)
                .name("North Face Travel Backpack")
                .description("Durable 30-liter backpack with laptop compartment")
                .dateOfManufacture(LocalDate.of(2026, 4, 14))
                .price(119.99)
                .category("accessories")
                .build();



        inventory.add(new InventoryItem(laptop, 100));
        inventory.add(new InventoryItem(headphones, 250));
        inventory.add(new InventoryItem(coffeeMaker, 30));  
        inventory.add(new InventoryItem(runningShoes, 200));
        inventory.add(new InventoryItem(backpack, 500));
    }


    @Override
    public List<InventoryItem> getAllInventoryItems() {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

        InventoryItem item = getInventoryItemByProductId(productId);

        if(item == null) {
            return false;
        }
        return inventory.remove(item);        
    }

    @Override
    public void clearInventory() {
        // TODO Auto-generated method stub
        inventory.clear();
    }


    @Override 
    public List<InventoryItem> getInventoryItemsSortedByName(){
        // TODO Auto-generated method stub
        List<InventoryItem> sortedList = new ArrayList<>(inventory);

        return sortedList;
    }

    @Override
    public List<InventoryItem> getInventoryItemsSortedByPrice(){
        
        // TODO Auto-generated method stub
        List<InventoryItem> sortedList = new ArrayList<>(inventory);

        return sortedList;
    }

    @Override 
    public List<InventoryItem> getInventoryItemsSortedByDate(){
        
        // TODO Auto-generated method stub
        List<InventoryItem> sortedList = new ArrayList<>(inventory);

        return sortedList;
    }


}
