package com.es.phoneshop.web.controller.exception;

public class InvalidFormatException extends RuntimeException {
    public InvalidFormatException() {
        super();
    }

    public InvalidFormatException(String message) {
        super(message);
    }
}