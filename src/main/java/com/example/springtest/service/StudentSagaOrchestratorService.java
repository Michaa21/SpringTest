package com.example.springtest.service;

import com.example.springtest.api.dto.request.ExternalStudentRequest;
import com.example.springtest.api.dto.request.StudentCreateRequest;
import com.example.springtest.api.dto.response.ExternalStudentResponse;
import com.example.springtest.api.dto.response.StudentResponse;
import com.example.springtest.client.ExternalStudentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentSagaOrchestratorService {

    private final StudentService studentService;
    private final ExternalStudentClient externalStudentClient;

    public StudentResponse createStudent(StudentCreateRequest request) {
        UUID studentId = UUID.randomUUID();
        ExternalStudentResponse externalResponse =
                externalStudentClient.createExternalStudent(
                        new ExternalStudentRequest(studentId, request.getName()));

        try {
            return studentService.create(request, externalResponse.getExtraInfo(), studentId);
        } catch (Exception ex) {
            if (externalResponse.getStudentId() != null) {
                externalStudentClient.deleteExternalStudent(externalResponse.getStudentId());
            }
            throw ex;
        }
    }
}