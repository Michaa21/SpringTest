package com.example.springtest.service;

import com.example.springtest.api.dto.request.LessonCreateRequest;
import com.example.springtest.api.dto.request.StudentCreateRequest;
import com.example.springtest.api.dto.response.ExternalStudentResponse;
import com.example.springtest.api.dto.response.StudentResponse;
import com.example.springtest.client.ExternalStudentClient;
import com.example.springtest.domain.Student;
import com.example.springtest.exception.EntityNotFoundException;
import com.example.springtest.repository.LessonRepository;
import com.example.springtest.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Testcontainers
@SpringBootTest
class StudentServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15");

    @Container
    static GenericContainer<?> redis =
            new GenericContainer<>(DockerImageName.parse("redis:7"))
                    .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired
    StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    LessonRepository lessonRepository;

    @MockBean
    ExternalStudentClient externalStudentClient;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        lessonRepository.deleteAll();
    }

    @Test
    void create_shouldSaveStudent() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Bob");

        LessonCreateRequest lesson = new LessonCreateRequest();
        lesson.setTitle("Math");
        request.setLessons(List.of(lesson));

        UUID studentId = UUID.randomUUID();
        String extraInfo = "extra-info-for-Bob";

        StudentResponse result = studentService.create(request, extraInfo, studentId);

        assertNotNull(result);
        assertEquals(studentId.toString(), result.getId());
        assertEquals("Bob", result.getName());
        assertEquals("extra-info-for-Bob", result.getExtra());
        assertNotNull(result.getLessons());
        assertEquals(1, result.getLessons().size());
        assertEquals("Math", result.getLessons().getFirst().getTitle());

        Student savedStudent = studentRepository.findWithLessonsById(studentId).orElseThrow();

        assertEquals("Bob", savedStudent.getName());
        assertEquals("extra-info-for-Bob", savedStudent.getExtra());
        assertEquals(1, savedStudent.getLessons().size());
    }

    @Test
    void getById_shouldReturnStudent() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Bob");

        LessonCreateRequest lesson = new LessonCreateRequest();
        lesson.setTitle("History");
        request.setLessons(List.of(lesson));

        UUID studentId = UUID.randomUUID();
        String extraInfo = "saved-extra-info";

        studentService.create(request, extraInfo, studentId);

        when(externalStudentClient.getStudentExtraInfo(studentId.toString()))
                .thenReturn(externalResponse(studentId, "external-extra-info"));
        StudentResponse result = studentService.getById(studentId);

        assertNotNull(result);
        assertEquals(studentId.toString(), result.getId());
        assertEquals("Bob", result.getName());
        assertEquals("external-extra-info", result.getExtra());
        assertNotNull(result.getLessons());
        assertEquals(1, result.getLessons().size());
        assertEquals("History", result.getLessons().getFirst().getTitle());
    }

    @Test
    void getById_shouldThrowException_whenStudentNotFound() {
        UUID id = UUID.randomUUID();

        assertThrows(EntityNotFoundException.class, () -> studentService.getById(id));
    }

    @Test
    void update_shouldUpdateStudent() {
        StudentCreateRequest createRequest = new StudentCreateRequest();
        createRequest.setName("Bob");

        LessonCreateRequest mathLesson = new LessonCreateRequest();
        mathLesson.setTitle("Math");
        createRequest.setLessons(List.of(mathLesson));

        UUID studentId = UUID.randomUUID();
        studentService.create(createRequest, "extra-info", studentId);

        StudentCreateRequest updateRequest = new StudentCreateRequest();
        updateRequest.setName("Alice");

        LessonCreateRequest historyLesson = new LessonCreateRequest();
        historyLesson.setTitle("History");
        updateRequest.setLessons(List.of(historyLesson));

        StudentResponse result = studentService.update(studentId, updateRequest);

        assertNotNull(result);
        assertEquals(studentId.toString(), result.getId());
        assertEquals("Alice", result.getName());
        assertNotNull(result.getLessons());
        assertEquals(1, result.getLessons().size());
        assertEquals("History", result.getLessons().getFirst().getTitle());

        Student updatedStudent = studentRepository.findWithLessonsById(studentId).orElseThrow();
        assertEquals("Alice", updatedStudent.getName());
        assertEquals(1, updatedStudent.getLessons().size());
        assertEquals("History", updatedStudent.getLessons().iterator().next().getTitle());
    }

    @Test
    void update_shouldThrowException_whenStudentNotFound() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Bob");
        request.setLessons(List.of());

        UUID id = UUID.randomUUID();

        assertThrows(EntityNotFoundException.class, () -> studentService.update(id, request));
    }

    @Test
    void delete_shouldRemoveStudent() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Bob");

        LessonCreateRequest lesson = new LessonCreateRequest();
        lesson.setTitle("Math");
        request.setLessons(List.of(lesson));

        UUID studentId = UUID.randomUUID();
        studentService.create(request, "extra-info", studentId);

        studentService.delete(studentId);

        assertTrue(studentRepository.findWithLessonsById(studentId).isEmpty());
    }

    @Test
    void delete_shouldThrowException_whenStudentNotFound() {
        UUID id = UUID.randomUUID();

        assertThrows(EntityNotFoundException.class, () -> studentService.delete(id));
    }

    @Test
    void create_shouldReuseExistingLesson_whenLessonAlreadyExists() {
        StudentCreateRequest firstRequest = new StudentCreateRequest();
        firstRequest.setName("Bob");

        LessonCreateRequest firstLesson = new LessonCreateRequest();
        firstLesson.setTitle("Math");
        firstRequest.setLessons(List.of(firstLesson));

        UUID firstStudentId = UUID.randomUUID();
        studentService.create(firstRequest, "extra-1", firstStudentId);

        StudentCreateRequest secondRequest = new StudentCreateRequest();
        secondRequest.setName("Alice");

        LessonCreateRequest secondLesson = new LessonCreateRequest();
        secondLesson.setTitle("Math");
        secondRequest.setLessons(List.of(secondLesson));

        UUID secondStudentId = UUID.randomUUID();
        studentService.create(secondRequest, "extra-2", secondStudentId);

        assertEquals(1, lessonRepository.findAll().size());
    }

    @Test
    void getById_shouldUseCache() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Bob");

        LessonCreateRequest lesson = new LessonCreateRequest();
        lesson.setTitle("History");
        request.setLessons(List.of(lesson));

        UUID studentId = UUID.randomUUID();
        studentService.create(request, "saved-extra-info", studentId);

        when(externalStudentClient.getStudentExtraInfo(studentId.toString()))
                .thenReturn(externalResponse(studentId, "external-extra-info"));
        StudentResponse first = studentService.getById(studentId);
        StudentResponse second = studentService.getById(studentId);

        assertNotNull(first);
        assertNotNull(second);
        assertEquals(studentId.toString(), first.getId());
        assertEquals(studentId.toString(), second.getId());
        assertEquals("external-extra-info", first.getExtra());
        assertEquals("external-extra-info", second.getExtra());

        verify(externalStudentClient).getStudentExtraInfo(studentId.toString());
    }

    @Test
    void getById_shouldEvictCacheAfterUpdate() {
        StudentCreateRequest createRequest = new StudentCreateRequest();
        createRequest.setName("Bob");

        LessonCreateRequest lesson = new LessonCreateRequest();
        lesson.setTitle("History");
        createRequest.setLessons(List.of(lesson));

        UUID studentId = UUID.randomUUID();
        studentService.create(createRequest, "saved-extra-info", studentId);

        when(externalStudentClient.getStudentExtraInfo(studentId.toString()))
                .thenReturn(
                        externalResponse(studentId, "external-extra-1"),
                        externalResponse(studentId, "external-extra-2")
                );
        StudentResponse first = studentService.getById(studentId);
        assertNotNull(first);
        assertEquals("external-extra-1", first.getExtra());

        StudentCreateRequest updateRequest = new StudentCreateRequest();
        updateRequest.setName("Alice");

        LessonCreateRequest updatedLesson = new LessonCreateRequest();
        updatedLesson.setTitle("Math");
        updateRequest.setLessons(List.of(updatedLesson));

        studentService.update(studentId, updateRequest);

        StudentResponse second = studentService.getById(studentId);
        assertNotNull(second);
        assertEquals(studentId.toString(), second.getId());
        assertEquals("Alice", second.getName());
        assertEquals("external-extra-2", second.getExtra());

        verify(externalStudentClient, times(2)).getStudentExtraInfo(studentId.toString());
    }

    @Test
    void getById_shouldEvictCacheAfterDelete() {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName("Bob");

        LessonCreateRequest lesson = new LessonCreateRequest();
        lesson.setTitle("History");
        request.setLessons(List.of(lesson));

        UUID studentId = UUID.randomUUID();
        studentService.create(request, "saved-extra-info", studentId);

        when(externalStudentClient.getStudentExtraInfo(studentId.toString()))
                .thenReturn(externalResponse(studentId, "external-extra-info"));
        StudentResponse cached = studentService.getById(studentId);
        assertNotNull(cached);
        assertEquals(studentId.toString(), cached.getId());

        studentService.delete(studentId);

        assertThrows(EntityNotFoundException.class, () -> studentService.getById(studentId));
    }

    private ExternalStudentResponse externalResponse(UUID studentId, String extraInfo) {
        ExternalStudentResponse response = new ExternalStudentResponse();
        response.setStudentId(studentId);
        response.setExtraInfo(extraInfo);
        return response;
    }
}