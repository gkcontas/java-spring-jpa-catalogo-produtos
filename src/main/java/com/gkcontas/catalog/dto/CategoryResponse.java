package com.gkcontas.catalog.dto;

import com.gkcontas.catalog.model.Category;

public record CategoryResponse(Long id, String name, String description) {

    public static CategoryResponse of(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
    }
}
