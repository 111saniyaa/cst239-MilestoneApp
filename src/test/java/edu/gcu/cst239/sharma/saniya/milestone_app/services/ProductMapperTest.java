package edu.gcu.cst239.sharma.saniya.milestone_app.services;

import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import edu.gcu.cst239.sharma.saniya.milestone_app.models.Product;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.ProductDto;

import edu.gcu.cst239.sharma.saniya.milestone_app.models.InventoryItem;
import edu.gcu.cst239.sharma.saniya.milestone_app.models.InventoryItemDto;

import edu.gcu.cst239.sharma.saniya.milestone_app.services.ProductMapper;


public class ProductMapperTest {
    

    // maybe first create test data for ProductDto and inventoryDto, then test the mapping methods toProduct and toProductDto

    ProductDto ice_cube_tray = new ProductDto(
        1,
        "Ice Cube Tray",
        "Silicone tray for making ice cubes with a lid to prevent spills.",
        "2023-01-01",
        19.99,
        "Kitchen"
    );

    ProductDto baking_mat = new ProductDto(
        2,
        "Silicone Baking Mat",
        "Non-stick baking mat for easy cleanup and even baking.",
        "2023-02-01",
        29.99,
        "Kitchen"
    );

    ProductDto office_chair = new ProductDto(
        3,
        "Home Office Chair",
        "Ergonomic chair with adjustable height and lumbar support for comfortable seating.",
        "2023-03-01",
        199.99,
        "Office"
    );

    // Here we verify the ProductDto-to-Product and Product-to-ProductDto mapping methods in the ProductMapper class.
    // And Date conversion is also verified in the mapping methods. The dateOfManufacture field in ProductDto is a String, 
    // while in Product it is a LocalDate. The mapping methods handle this conversion correctly.
    @Test 
    void checkProductDtoAndProductMapping() {
        Product product = ProductMapper.toProduct(ice_cube_tray);
        ProductDto productDto = ProductMapper.toProductDto(product);

        assertAll(() -> assertEquals(ice_cube_tray.id(), product.getId()),
                  () -> assertEquals(ice_cube_tray.name(), product.getName()),
                  () -> assertEquals(ice_cube_tray.description(), product.getDescription()),
                  () -> assertEquals(ice_cube_tray.dateOfManufacture(), product.getDateOfManufacture().toString()),
                  () -> assertEquals(ice_cube_tray.price(), product.getPrice()),
                  () -> assertEquals(ice_cube_tray.category(), product.getCategory())
        );

        assertAll(() -> assertEquals(product.getId(), productDto.id()),
                  () -> assertEquals(product.getName(), productDto.name()),
                  () -> assertEquals(product.getDescription(), productDto.description()),
                  () -> assertEquals(product.getDateOfManufacture().toString(), productDto.dateOfManufacture()),
                  () -> assertEquals(product.getPrice(), productDto.price()),
                  () -> assertEquals(product.getCategory(), productDto.category())
        );
    }

    @Test
    void convertsIsoDateTextToLocalDate() {
        Product product = ProductMapper.toProduct(ice_cube_tray);

        assertEquals(LocalDate.of(2023, 1, 1), product.getDateOfManufacture());
    }

    @Test 
    void defaultBlankCategoryIsHandled() {
        ProductDto productDto = new ProductDto(
            4,
            "Test Product",
            "Test Description",
            "2023-04-01",
            49.99,
            ""   // Blank category
        );

        Product product = ProductMapper.toProduct(productDto);
        assertEquals("no-category", product.getCategory());

        ProductDto mappedBackDto = ProductMapper.toProductDto(product);
        assertEquals("no-category", mappedBackDto.category());
    }

    @Test
    void rejectsDateThatIsNotAnIsoDate() {
        ProductDto dto = new ProductDto(8, "Notebook", "Lined notebook", "03/14/2025", 3.49, "Office");

        assertThrows(DateTimeParseException.class, () -> ProductMapper.toProduct(dto));   
    }

    @Test
    void returnsNullWhenDtoIsNull() {
        assertThrows(IllegalArgumentException.class, () -> ProductMapper.toProduct(null));
    }

    @Test
    void mapsProductBackToDtoAndPreservesValues() {
        Product product = ProductMapper.toProduct(office_chair);

        ProductDto mappedDto = ProductMapper.toProductDto(product);

        assertEquals(office_chair, mappedDto);
    }


}
