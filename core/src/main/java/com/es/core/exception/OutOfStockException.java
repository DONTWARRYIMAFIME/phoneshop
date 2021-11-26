package com.es.core.exception;

public class OutOfStockException extends IllegalArgumentException {
    public OutOfStockException(String message) {
        super(message);
    }

    public OutOfStockException(Long stockRequested, Long stockAvailable) {
        super(String.format("Out of stock, available: %d. But requested %d", stockAvailable, stockRequested));
    }
}
