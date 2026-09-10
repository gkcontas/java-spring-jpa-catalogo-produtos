package com.gustavo.catalog.dto;

import com.gustavo.catalog.model.Category;

public record CategoryResponse(Long id, String name, String description) {

    public static CategoryResponse of(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
    }
}
