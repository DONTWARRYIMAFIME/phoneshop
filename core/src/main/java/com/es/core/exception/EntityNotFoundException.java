package com.es.core.exception;

public class EntityNotFoundException extends IllegalArgumentException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entity, Long id) {
        super("Entity: " + entity + " with ID: " + id + " was not found");
    }

}
