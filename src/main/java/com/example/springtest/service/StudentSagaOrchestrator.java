package com.example.springtest.service;

import com.example.springtest.api.dto.request.ExternalStudentRequest;
import com.example.springtest.api.dto.request.StudentCreateRequest;
import com.example.springtest.api.dto.response.ExternalStudentResponse;
import com.example.springtest.api.dto.response.StudentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentSagaOrchestrator {

    private final StudentService studentService;
    private final ExternalServiceCaller externalServiceCaller;

    public StudentResponse createStudent(StudentCreateRequest request) {
        UUID studentId = UUID.randomUUID();
        ExternalStudentResponse externalResponse =
                externalServiceCaller.createExternalStudent(
                        new ExternalStudentRequest(studentId, request.getName()));

        try {
            return studentService.create(request, externalResponse.getExtraInfo(), studentId);
        } catch (Exception ex) {
            if (externalResponse.getId() != null) {
                externalServiceCaller.deleteExternalStudent(externalResponse.getId());
            }
            throw ex;
        }
    }
}