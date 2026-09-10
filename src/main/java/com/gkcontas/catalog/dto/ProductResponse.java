package com.gkcontas.catalog.dto;

import com.gkcontas.catalog.model.Product;
import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stockQuantity,
        CategoryResponse category
) {

    public static ProductResponse of(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                CategoryResponse.of(product.getCategory())
        );
    }
}
