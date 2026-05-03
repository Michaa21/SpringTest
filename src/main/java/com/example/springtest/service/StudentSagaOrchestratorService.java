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

    public StudentResponse createStudent(StudentCreateRequest request) {
        UUID studentId = UUID.randomUUID();

        ExternalStudentRequest externalRequest = buildExternalStudentRequest(request, studentId);

        ExternalStudentResponse externalResponse =
                externalStudentClient.createExternalStudent(externalRequest);

        try {
            return studentService.create(request, externalResponse, studentId);
        } catch (Exception ex) {
            compensateExternalStudentCreation(externalResponse);
            throw ex;
        }
    }

    private ExternalStudentRequest buildExternalStudentRequest(
            StudentCreateRequest request,
            UUID studentId
    ) {
        return new ExternalStudentRequest(
                studentId,
                request.getName(),
                request.getEmail(),
                request.getAge()
        );
    }

    private void compensateExternalStudentCreation(ExternalStudentResponse externalResponse) {
        if (externalResponse.getStudentId() == null) {
            log.warn("External student compensation skipped because studentId is null");
            return;
        }

        try {
            externalStudentClient.deleteExternalStudent(externalResponse.getStudentId());
            log.info(
                    "External student with studentId {} deleted during compensation",
                    externalResponse.getStudentId()
            );
        } catch (Exception compensationException) {
            log.error(
                    "Failed to compensate external student creation. studentId={}",
                    externalResponse.getStudentId(),
                    compensationException
            );
        }
    }
}