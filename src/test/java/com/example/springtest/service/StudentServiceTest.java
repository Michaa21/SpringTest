package com.example.springtest.service;

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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
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

        when(studentRepository.findWithLessonsById(id))
                .thenReturn(Optional.of(student));

        when(studentApiMapper.toResponse(student))
                .thenReturn(response);

        when(externalServiceCaller.getExtra(id))
                .thenReturn("extra");

        StudentResponse result = studentService.getById(id);

        assertNotNull(result);
        verify(studentRepository).findWithLessonsById(id);
        verify(studentApiMapper).toResponse(student);
        verify(externalServiceCaller).getExtra(id);
    }

    @Test
    void getById_shouldThrowException_whenStudentNotFound() {
        UUID id = UUID.randomUUID();

        when(studentRepository.findWithLessonsById(id))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            studentService.getById(id);
        });

        verify(studentRepository).findWithLessonsById(id);
        verifyNoInteractions(externalServiceCaller);
    }
}
