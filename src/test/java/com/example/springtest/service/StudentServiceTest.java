package com.example.springtest.service;

import com.example.springtest.api.dto.request.StudentCreateRequest;
import com.example.springtest.api.dto.response.ExternalStudentResponse;
import com.example.springtest.api.dto.response.StudentResponse;
import com.example.springtest.domain.Student;
import com.example.springtest.exception.EntityNotFoundException;
import com.example.springtest.mapper.StudentApiMapper;
import com.example.springtest.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentApiMapper studentApiMapper;

    @Mock
    private ExternalServiceCaller externalServiceCaller;

    @InjectMocks
    private StudentService studentService;

    @Test
    void getById_shouldReturnStudent() {
        UUID id = UUID.randomUUID();

        Student student = new Student();
        student.setId(id);

        StudentResponse response = new StudentResponse();
        response.setId(id.toString());
        response.setName("Bob");

        when(studentRepository.findWithLessonsById(id))
                .thenReturn(Optional.of(student));

        when(studentApiMapper.toResponse(student))
                .thenReturn(response);

        when(externalServiceCaller.getExtra(id))
                .thenReturn("extra-info-for-" + id);

        StudentResponse result = studentService.getById(id);

        assertNotNull(result);
        assertEquals("extra-info-for-" + id, result.getExtra());

        verify(studentRepository).findWithLessonsById(id);
        verify(studentApiMapper).toResponse(student);
        verify(externalServiceCaller).getExtra(id);
    }

    @Test
    void create_shouldSaveStudentWithExtra() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Misha");
        request.setLessons(Collections.emptyList());

        Student student = new Student();
        Student saved = new Student();
        StudentResponse response = new StudentResponse();

        ExternalStudentResponse externalResponse = new ExternalStudentResponse();
        externalResponse.setExtraInfo("extra-info-for-Misha");

        when(studentApiMapper.toEntity(request)).thenReturn(student);
        when(externalServiceCaller.createExternalStudent("extra-info-for-Misha"))
                .thenReturn(externalResponse);
        when(studentRepository.save(student)).thenReturn(saved);
        when(studentApiMapper.toResponse(saved)).thenReturn(response);

        StudentResponse result = studentService.create(request);

        assertNotNull(result);
        assertEquals("extra-info-for-Misha", student.getExtra());

        verify(studentApiMapper).toEntity(request);
        verify(externalServiceCaller).createExternalStudent("extra-info-for-Misha");
        verify(studentRepository).save(student);
        verify(studentApiMapper).toResponse(saved);
    }

    @Test
    void getById_shouldThrowException_whenStudentNotFound() {
        UUID id = UUID.randomUUID();

        when(studentRepository.findWithLessonsById(id))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> studentService.getById(id));

        verify(studentRepository).findWithLessonsById(id);
        verifyNoInteractions(externalServiceCaller);
    }

    @Test
    void getById_shouldReturnStudentWithFallbackExtra() {
        UUID id = UUID.randomUUID();

        Student student = new Student();
        student.setId(id);

        StudentResponse response = new StudentResponse();
        response.setId(id.toString());
        response.setName("Bob");

        when(studentRepository.findWithLessonsById(id))
                .thenReturn(Optional.of(student));

        when(studentApiMapper.toResponse(student))
                .thenReturn(response);

        when(externalServiceCaller.getExtra(id))
                .thenReturn("no extra info");

        StudentResponse result = studentService.getById(id);

        assertNotNull(result);
        assertEquals("no extra info", result.getExtra());

        verify(studentRepository).findWithLessonsById(id);
        verify(studentApiMapper).toResponse(student);
        verify(externalServiceCaller).getExtra(id);
    }

    @Test
    void create_shouldSaveStudentWithFallbackExtra() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Misha");
        request.setLessons(Collections.emptyList());

        Student student = new Student();
        Student saved = new Student();
        StudentResponse response = new StudentResponse();

        ExternalStudentResponse externalResponse = new ExternalStudentResponse();
        externalResponse.setExtraInfo("no extra info");

        when(studentApiMapper.toEntity(request)).thenReturn(student);
        when(externalServiceCaller.createExternalStudent("extra-info-for-Misha"))
                .thenReturn(externalResponse);
        when(studentRepository.save(student)).thenReturn(saved);
        when(studentApiMapper.toResponse(saved)).thenReturn(response);

        StudentResponse result = studentService.create(request);

        assertNotNull(result);
        assertEquals("no extra info", student.getExtra());

        verify(studentApiMapper).toEntity(request);
        verify(externalServiceCaller).createExternalStudent("extra-info-for-Misha");
        verify(studentRepository).save(student);
        verify(studentApiMapper).toResponse(saved);
    }
}