package edu.gcu.cst239.sharma.saniya.milestone_app.models;

public record ProductDto(
        int id,
        String name,
        String description,
        String dateOfManufacture, // Keeping Date as string, so the JSON contract remains simple and language-neutral
        double price,
        String category) {
}


