package com.gkcontas.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gkcontas.catalog.dto.CategoryRequest;
import com.gkcontas.catalog.exception.ResourceNotFoundException;
import com.gkcontas.catalog.model.Category;
import com.gkcontas.catalog.repository.CategoryRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void shouldCreateCategory() {
        Category category = new Category("Books", "Books in general");
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        var response = categoryService.create(new CategoryRequest("Books", "Books in general"));

        assertThat(response.name()).isEqualTo("Books");
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        when(categoryRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.findById(42L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category");
    }

    @Test
    void shouldDeleteExistingCategory() {
        Category category = new Category("Books", "Books in general");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.delete(1L);

        verify(categoryRepository).delete(category);
    }
}
