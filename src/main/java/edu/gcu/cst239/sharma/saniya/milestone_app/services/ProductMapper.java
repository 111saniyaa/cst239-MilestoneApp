package edu.gcu.cst239.sharma.saniya.milestone_app.services;

import edu.gcu.cst239.sharma.saniya.milestone_app.models.Product;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.ProductDto;

public final class ProductMapper {

    private ProductMapper() {
        // Private constructor to prevent instantiation
    }


    public static Product toProduct(ProductDto productDto) {

        if(productDto == null) {
            throw new IllegalArgumentException("ProductDto cannot be null");
        }

        java.time.LocalDate dateOfManufacture = java.time.LocalDate.parse(productDto.dateOfManufacture());

        return Product.builder()
                .id(productDto.id())
                .name(productDto.name())
                .description(productDto.description())
                .dateOfManufacture(dateOfManufacture)
                .price(productDto.price())
                .category(productDto.category())
                .build();
    }

    public static ProductDto toProductDto(Product product) {

        if(product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        String dateOfManufacture = product.getDateOfManufacture().toString();

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                dateOfManufacture,
                product.getPrice(),
                product.getCategory()
        );
    }
   

    
}