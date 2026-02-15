package com.example.springtest.exception;

public class StudentNotFoundException extends  EntityNotFoundException{
    public StudentNotFoundException(Long id) {
        super("Студент с id " + id + " не найден");
    }
}
