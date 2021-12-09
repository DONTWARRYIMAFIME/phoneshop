package com.es.core.exception;

public class PhoneNotFoundException extends EntityNotFoundException {

    public PhoneNotFoundException(String message) {
        super(message);
    }

    public PhoneNotFoundException(Long id) {
        super("Phone", id);
    }
}
