package com.example.springtest.service;

import com.example.springtest.api.dto.request.StudentCreateRequest;
import com.example.springtest.api.dto.response.ExternalStudentResponse;
import com.example.springtest.api.dto.response.StudentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentSagaOrchestratorTest {

    @Mock
    private StudentService studentService;

    @Mock
    private ExternalServiceCaller externalServiceCaller;

    @InjectMocks
    private StudentSagaOrchestrator studentSagaOrchestrator;

    @Test
    void createStudent_shouldReturnStudentWithExtra() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Bob");
        request.setLessons(Collections.emptyList());

        ExternalStudentResponse externalResponse = new ExternalStudentResponse();
        externalResponse.setStudentId(UUID.randomUUID());
        externalResponse.setExtraInfo("extra-info-for-Bob");

        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setId(externalResponse.getStudentId().toString());
        studentResponse.setName("Bob");
        studentResponse.setExtra("extra-info-for-Bob");

        when(externalServiceCaller.createExternalStudent(any())).thenReturn(externalResponse);
        when(studentService.create(any(StudentCreateRequest.class), anyString(), any(UUID.class)))
                .thenReturn(studentResponse);

        StudentResponse result = studentSagaOrchestrator.createStudent(request);

        assertNotNull(result);
        assertEquals(externalResponse.getStudentId().toString(), result.getId());
        assertEquals("Bob", result.getName());
        assertEquals("extra-info-for-Bob", result.getExtra());
    }

    @Test
    void createStudent_shouldReturnStudentWithFallbackExtra() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Bob");
        request.setLessons(Collections.emptyList());

        ExternalStudentResponse externalResponse = new ExternalStudentResponse();
        externalResponse.setStudentId(UUID.randomUUID());
        externalResponse.setExtraInfo("no extra info");

        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setId(externalResponse.getStudentId().toString());
        studentResponse.setName("Bob");
        studentResponse.setExtra("no extra info");

        when(externalServiceCaller.createExternalStudent(any())).thenReturn(externalResponse);
        when(studentService.create(any(StudentCreateRequest.class), anyString(), any(UUID.class)))
                .thenReturn(studentResponse);

        StudentResponse result = studentSagaOrchestrator.createStudent(request);

        assertNotNull(result);
        assertEquals(externalResponse.getStudentId().toString(), result.getId());
        assertEquals("Bob", result.getName());
        assertEquals("no extra info", result.getExtra());
    }

    @Test
    void createStudent_shouldDeleteExternalStudent_whenStudentSaveFails() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Bob");
        request.setLessons(Collections.emptyList());

        UUID externalStudentId = UUID.randomUUID();

        ExternalStudentResponse externalResponse = new ExternalStudentResponse();
        externalResponse.setStudentId(externalStudentId);
        externalResponse.setExtraInfo("extra-info-for-Bob");

        when(externalServiceCaller.createExternalStudent(any())).thenReturn(externalResponse);
        when(studentService.create(any(StudentCreateRequest.class), anyString(), any(UUID.class)))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> studentSagaOrchestrator.createStudent(request));

        verify(externalServiceCaller).deleteExternalStudent(externalStudentId);
    }

    @Test
    void createStudent_shouldNotDeleteExternalStudent_whenExternalStudentIdIsNull() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Bob");
        request.setLessons(Collections.emptyList());

        ExternalStudentResponse externalResponse = new ExternalStudentResponse();
        externalResponse.setStudentId(null);
        externalResponse.setExtraInfo("extra-info-for-Bob");

        when(externalServiceCaller.createExternalStudent(any())).thenReturn(externalResponse);
        when(studentService.create(any(StudentCreateRequest.class), anyString(), any(UUID.class)))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> studentSagaOrchestrator.createStudent(request));

        verify(externalServiceCaller, never()).deleteExternalStudent(any());
    }
}