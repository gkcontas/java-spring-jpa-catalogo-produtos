package com.gustavo.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.gustavo.catalog.dto.ProductRequest;
import com.gustavo.catalog.dto.StockRequest;
import com.gustavo.catalog.exception.InsufficientStockException;
import com.gustavo.catalog.exception.ResourceNotFoundException;
import com.gustavo.catalog.model.Category;
import com.gustavo.catalog.model.Product;
import com.gustavo.catalog.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        category = new Category("Electronics", "Electronic products");
        product = new Product("Mouse", "Wireless mouse", new BigDecimal("99.90"), 10, category);
    }

    @Test
    void shouldCreateProductWithExistingCategory() {
        when(categoryService.findEntityById(1L)).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductRequest request = new ProductRequest("Mouse", "Wireless mouse", new BigDecimal("99.90"), 10, 1L);

        var response = productService.create(request);

        assertThat(response.name()).isEqualTo("Mouse");
        assertThat(response.stockQuantity()).isEqualTo(10);
        assertThat(response.category().name()).isEqualTo("Electronics");
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product");
    }

    @Test
    void shouldIncreaseStockSuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        var response = productService.increaseStock(1L, new StockRequest(5));

        assertThat(response.stockQuantity()).isEqualTo(15);
    }

    @Test
    void shouldThrowExceptionWhenDecreasingMoreThanAvailableStock() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> productService.decreaseStock(1L, new StockRequest(50)))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Insufficient stock");
    }

    @Test
    void shouldDecreaseStockSuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        var response = productService.decreaseStock(1L, new StockRequest(4));

        assertThat(response.stockQuantity()).isEqualTo(6);
    }
}
