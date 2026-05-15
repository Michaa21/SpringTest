package com.example.springtest.service;

import com.example.springtest.api.dto.request.ExternalStudentRequest;
import com.example.springtest.api.dto.request.StudentCreateRequest;
import com.example.springtest.api.dto.response.ExternalStudentResponse;
import com.example.springtest.api.dto.response.StudentResponse;
import com.example.springtest.client.ExternalStudentClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentSagaOrchestratorService {

    private final StudentService studentService;
    private final ExternalStudentClient externalStudentClient;
    private final ExternalStudentCompensationTaskService compensationTaskService;

    public StudentResponse createStudent(StudentCreateRequest request) {
        UUID localStudentId = UUID.randomUUID();

        ExternalStudentRequest externalRequest = buildExternalStudentRequest(request, localStudentId);

        ExternalStudentResponse externalResponse =
                externalStudentClient.createExternalStudent(externalRequest);

        try {
            return studentService.create(request, externalResponse, localStudentId);
        } catch (Exception ex) {
            compensationTaskService.createTask(externalResponse.getStudentId());
            throw ex;
        }
    }

    private ExternalStudentRequest buildExternalStudentRequest(
            StudentCreateRequest request,
            UUID localStudentId
    ) {
        return new ExternalStudentRequest(
                localStudentId,
                request.getName(),
                request.getEmail(),
                request.getAge()
        );
    }
}