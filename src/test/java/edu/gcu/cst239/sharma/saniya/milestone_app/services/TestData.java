package edu.gcu.cst239.sharma.saniya.milestone_app.services;

import java.time.LocalDate;
import java.util.List;

import edu.gcu.cst239.sharma.saniya.milestone_app.models.InventoryItem;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.Product;



final class TestData {

    private TestData() {}

    static Product product(int id, String name, String description,  LocalDate date,double price,String category) {
        
        return Product.builder()
                .id(id)
                .name(name)
                .description(description)
                .dateOfManufacture(date)
                .price(price)
                .category(category)
                .build();
    }

    static InventoryItem inventoryItem(int id, String name, String description,  LocalDate date,double price, int quantity, String category) {
        return new InventoryItem(product(id, name, description, date, price, category), quantity);
    }


    static InventoryManager manager(){
        return new InventoryManager(List.of(
                inventoryItem(1, "Speaker", "wireless Bluetooth speaker",
                    LocalDate.of(2023, 1, 1), 10.0, 5, "electronics"),
                inventoryItem(2, "Cable", "USB-C Cable", 
                    LocalDate.of(2023, 2, 1), 20.0, 10, "accessories"),
                inventoryItem(3, "Headphones", "wireless Noise-cancelling headphones", 
                    LocalDate.of(2023, 3, 1), 30.0, 15, "audio")
        ));
    }

    static List<Integer> ids(List<InventoryItem> items) {
        return items.stream().map(item -> item.getProduct().getId()).toList();
    }
    
}
