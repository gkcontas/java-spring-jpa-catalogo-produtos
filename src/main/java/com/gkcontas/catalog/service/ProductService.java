package com.gkcontas.catalog.service;

import com.gkcontas.catalog.dto.ProductRequest;
import com.gkcontas.catalog.dto.ProductResponse;
import com.gkcontas.catalog.dto.StockRequest;
import com.gkcontas.catalog.exception.ResourceNotFoundException;
import com.gkcontas.catalog.model.Category;
import com.gkcontas.catalog.model.Product;
import com.gkcontas.catalog.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> list(Long categoryId, Pageable pageable) {
        Page<Product> page = categoryId != null
                ? productRepository.findByCategoryId(categoryId, pageable)
                : productRepository.findAll(pageable);
        return page.map(ProductResponse::of);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return ProductResponse.of(findEntityById(id));
    }

    private Product findEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    public ProductResponse create(ProductRequest request) {
        Category category = categoryService.findEntityById(request.categoryId());
        int initialStockQuantity = request.initialStockQuantity() != null ? request.initialStockQuantity() : 0;
        Product product = new Product(
                request.name(),
                request.description(),
                request.price(),
                initialStockQuantity,
                category
        );
        return ProductResponse.of(productRepository.save(product));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findEntityById(id);
        Category category = categoryService.findEntityById(request.categoryId());
        product.updateDetails(request.name(), request.description(), request.price(), category);
        return ProductResponse.of(product);
    }

    public void delete(Long id) {
        Product product = findEntityById(id);
        productRepository.delete(product);
    }

    public ProductResponse increaseStock(Long id, StockRequest request) {
        Product product = findEntityById(id);
        product.increaseStock(request.quantity());
        return ProductResponse.of(product);
    }

    public ProductResponse decreaseStock(Long id, StockRequest request) {
        Product product = findEntityById(id);
        product.decreaseStock(request.quantity());
        return ProductResponse.of(product);
    }
}
