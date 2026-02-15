package com.example.springtest.exception;

import java.util.UUID;

public class UserNotFoundException extends EntityNotFoundException{
    public UserNotFoundException(UUID id) {
        super("Пользователь с id " + id + " не найден");
    }
}
