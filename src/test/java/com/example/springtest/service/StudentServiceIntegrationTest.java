package com.example.springtest.service;

import com.example.springtest.api.dto.request.LessonCreateRequest;
import com.example.springtest.api.dto.request.StudentCreateRequest;
import com.example.springtest.api.dto.response.ExternalStudentResponse;
import com.example.springtest.api.dto.response.StudentResponse;
import com.example.springtest.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Testcontainers
@SpringBootTest
class StudentServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @MockBean
    private ExternalServiceCaller externalServiceCaller;

    @Test
    void create_shouldSaveStudentWithExtra() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Misha");

        LessonCreateRequest lesson = new LessonCreateRequest();
        lesson.setTitle("Math");
        request.setLessons(List.of(lesson));

        ExternalStudentResponse externalResponse = new ExternalStudentResponse();
        externalResponse.setExtraInfo("extra-info-for-Misha");

        when(externalServiceCaller.createExternalStudent("extra-info-for-Misha"))
                .thenReturn(externalResponse);

        StudentResponse result = studentService.create(request);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Misha", result.getName());
        assertEquals("extra-info-for-Misha", result.getExtra());
        assertNotNull(result.getLessons());
        assertEquals(1, result.getLessons().size());
        assertEquals("Math", result.getLessons().getFirst().getTitle());

        var savedStudent = studentRepository.findWithLessonsById(UUID.fromString(result.getId()));

        assertTrue(savedStudent.isPresent());
        assertEquals("Misha", savedStudent.get().getName());
        assertEquals("extra-info-for-Misha", savedStudent.get().getExtra());
        assertEquals(1, savedStudent.get().getLessons().size());
    }
}