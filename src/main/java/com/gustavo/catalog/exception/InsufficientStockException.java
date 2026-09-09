package com.gustavo.catalog.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(Long productId, int availableQuantity, int requestedQuantity) {
        super("Insufficient stock for product %d: available %d, requested %d"
                .formatted(productId, availableQuantity, requestedQuantity));
    }
}
