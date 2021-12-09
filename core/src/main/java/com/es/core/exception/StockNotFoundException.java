package com.es.core.exception;

public class StockNotFoundException extends EntityNotFoundException {
    public StockNotFoundException(String message) {
        super(message);
    }

    public StockNotFoundException(Long phoneId) {
        super("Stock", phoneId);
    }
}
