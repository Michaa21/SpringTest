package com.example.springtest.exception;

public class AuthorNotFoundException extends EntityNotFoundException{

    public AuthorNotFoundException(Long id) {
        super("Автор с id " + id + " не найден");
    }
}
