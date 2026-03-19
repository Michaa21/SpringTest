package com.example.springtest.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityName, Object id) {
        super(entityName + " not found: " + id);
    }
}
