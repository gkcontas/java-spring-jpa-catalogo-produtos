package com.gustavo.catalog.service;

import com.gustavo.catalog.dto.CategoryRequest;
import com.gustavo.catalog.dto.CategoryResponse;
import com.gustavo.catalog.exception.ResourceNotFoundException;
import com.gustavo.catalog.model.Category;
import com.gustavo.catalog.repository.CategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> list() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        return CategoryResponse.of(findEntityById(id));
    }

    @Transactional(readOnly = true)
    public Category findEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }

    public CategoryResponse create(CategoryRequest request) {
        Category category = new Category(request.name(), request.description());
        return CategoryResponse.of(categoryRepository.save(category));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = findEntityById(id);
        category.setName(request.name());
        category.setDescription(request.description());
        return CategoryResponse.of(category);
    }

    public void delete(Long id) {
        Category category = findEntityById(id);
        categoryRepository.delete(category);
    }
}
