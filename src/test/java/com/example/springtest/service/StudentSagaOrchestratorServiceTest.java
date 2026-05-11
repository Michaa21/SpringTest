package com.example.springtest.service;

import com.example.springtest.api.dto.request.StudentCreateRequest;
import com.example.springtest.api.dto.response.ExternalStudentResponse;
import com.example.springtest.api.dto.response.StudentResponse;
import com.example.springtest.client.ExternalStudentClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentSagaOrchestratorServiceTest {

    @Mock
    private StudentService studentService;

    @Mock
    private ExternalStudentClient externalStudentClient;

    @Mock
    private ExternalStudentCompensationTaskService compensationTaskService;

    @InjectMocks
    private StudentSagaOrchestratorService studentSagaOrchestratorService;

    @Test
    void createStudent_shouldReturnStudentWithExternalData() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Bob");
        request.setLessons(Collections.emptyList());

        UUID studentId = UUID.randomUUID();

        ExternalStudentResponse externalResponse = new ExternalStudentResponse();
        externalResponse.setStudentId(studentId);
        externalResponse.setExtraInfo("extra-info-for-Bob");
        externalResponse.setEmail("bob@mail.com");
        externalResponse.setAge(20);

        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setId(studentId);
        studentResponse.setName("Bob");
        studentResponse.setExtra("extra-info-for-Bob");
        studentResponse.setEmail("bob@mail.com");
        studentResponse.setAge(20);

        when(externalStudentClient.createExternalStudent(any())).thenReturn(externalResponse);
        when(studentService.create(any(StudentCreateRequest.class), any(ExternalStudentResponse.class), any(UUID.class)))
                .thenReturn(studentResponse);

        StudentResponse result = studentSagaOrchestratorService.createStudent(request);

        assertNotNull(result);
        assertEquals(studentId, result.getId());
        assertEquals("Bob", result.getName());
        assertEquals("extra-info-for-Bob", result.getExtra());
        assertEquals("bob@mail.com", result.getEmail());
        assertEquals(20, result.getAge());

        verify(externalStudentClient).createExternalStudent(any());
        verify(studentService).create(any(StudentCreateRequest.class), eq(externalResponse), any(UUID.class));
    }

    @Test
    void createStudent_shouldReturnStudentWithFallbackExtra() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Bob");
        request.setLessons(Collections.emptyList());

        UUID studentId = UUID.randomUUID();

        ExternalStudentResponse externalResponse = new ExternalStudentResponse();
        externalResponse.setStudentId(studentId);
        externalResponse.setExtraInfo("no extra info");
        externalResponse.setEmail("bob@mail.com");
        externalResponse.setAge(20);

        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setId(studentId);
        studentResponse.setName("Bob");
        studentResponse.setExtra("no extra info");
        studentResponse.setEmail("bob@mail.com");
        studentResponse.setAge(20);

        when(externalStudentClient.createExternalStudent(any())).thenReturn(externalResponse);
        when(studentService.create(any(StudentCreateRequest.class), any(ExternalStudentResponse.class), any(UUID.class)))
                .thenReturn(studentResponse);

        StudentResponse result = studentSagaOrchestratorService.createStudent(request);

        assertNotNull(result);
        assertEquals(studentId, result.getId());
        assertEquals("Bob", result.getName());
        assertEquals("no extra info", result.getExtra());
        assertEquals("bob@mail.com", result.getEmail());
        assertEquals(20, result.getAge());
    }

    @Test
    void createStudent_shouldCreateCompensationTask_whenStudentSaveFails() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Bob");
        request.setLessons(Collections.emptyList());

        UUID externalStudentId = UUID.randomUUID();

        ExternalStudentResponse externalResponse = new ExternalStudentResponse();
        externalResponse.setStudentId(externalStudentId);
        externalResponse.setExtraInfo("extra-info-for-Bob");
        externalResponse.setEmail("bob@mail.com");
        externalResponse.setAge(20);

        when(externalStudentClient.createExternalStudent(any())).thenReturn(externalResponse);
        when(studentService.create(any(StudentCreateRequest.class), any(ExternalStudentResponse.class), any(UUID.class)))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> studentSagaOrchestratorService.createStudent(request));

        verify(compensationTaskService).createTask(externalStudentId);
        verify(externalStudentClient, never()).compensateStudentCreation(any());
    }
}