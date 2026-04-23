package com.example.springtest.service;

import com.example.springtest.api.dto.request.ExternalStudentRequest;
import com.example.springtest.api.dto.response.ExternalStudentResponse;
import com.example.springtest.client.ExternalStudentClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ExternalServiceCallerTest {

    @Mock
    private ExternalStudentClient externalStudentClient;

    @InjectMocks
    private ExternalServiceCaller externalServiceCaller;

    @Test
    void getExtraFallback_shouldReturnDefaultValue() {
        UUID studentId = UUID.randomUUID();

        String result = externalServiceCaller.getExtraFallback(
                studentId,
                new RuntimeException("external service unavailable")
        );

        assertEquals("no extra info", result);
    }

    @Test
    void createExternalStudentFallback_shouldReturnResponseWithDefaultExtra() {
        ExternalStudentRequest request = new ExternalStudentRequest();
        request.setStudentId(UUID.randomUUID());
        request.setName("Bob");

        ExternalStudentResponse result = externalServiceCaller.createExternalStudentFallback(
                request,
                new RuntimeException("external service unavailable")
        );

        assertEquals("no extra info", result.getExtraInfo());
    }
}