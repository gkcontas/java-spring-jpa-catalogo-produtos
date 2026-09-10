package com.gkcontas.catalog.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductRequest(

        @NotBlank(message = "name is required")
        @Size(max = 150, message = "name must be at most 150 characters long")
        String name,

        @Size(max = 1000, message = "description must be at most 1000 characters long")
        String description,

        @NotNull(message = "price is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "price cannot be negative")
        BigDecimal price,

        @PositiveOrZero(message = "initialStockQuantity cannot be negative")
        Integer initialStockQuantity,

        @NotNull(message = "categoryId is required")
        @Min(value = 1, message = "categoryId is invalid")
        Long categoryId
) {
}
