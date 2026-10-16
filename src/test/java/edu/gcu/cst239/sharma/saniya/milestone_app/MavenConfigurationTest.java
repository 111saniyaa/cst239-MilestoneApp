package edu.gcu.cst239.sharma.saniya.milestone_app;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import edu.gcu.cst239.sharma.saniya.milestone_app.models.Product;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MavenConfigurationTest {

    @Test 
    void testMavenConfiguration() {
        // This test is intentionally left empty.
        // It serves as a placeholder to verify that the Maven configuration is set up correctly.

        Product product = Product.builder()
                .id(99)
                .name("Test Product")
                .description("This is a test product.")
                .price(9.99)
                .dateOfManufacture(LocalDate.of(2026,10,15))
                .build();

        assertEquals(99, product.getId());
    }
    
}
