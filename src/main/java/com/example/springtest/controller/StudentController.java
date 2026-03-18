package com.example.springtest.controller;

import com.example.springtest.api.StudentApi;
import com.example.springtest.api.dto.request.StudentCreateRequest;
import com.example.springtest.api.dto.response.StudentResponse;
import com.example.springtest.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class StudentController implements StudentApi {

    private final StudentService studentService;

    @Override
    public ResponseEntity<StudentResponse> createStudent(StudentCreateRequest request) {
        return ResponseEntity.ok(studentService.create(request));
    }

    @Override
    public ResponseEntity<StudentResponse> getStudentById(UUID id) {
        return ResponseEntity.ok(studentService.getById(id));
    }

    @Override
    public ResponseEntity<StudentResponse> updateStudent(UUID id, StudentCreateRequest request) {
        return ResponseEntity.ok(studentService.update(id, request));
    }

    @Override
    public ResponseEntity<Void> deleteStudent(UUID id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

