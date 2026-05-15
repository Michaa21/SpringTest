package com.example.springtest;

import com.example.springtest.api.dto.request.LessonCreateRequest;
import com.example.springtest.api.dto.request.StudentCreateRequest;
import com.example.springtest.api.dto.response.ExternalStudentResponse;
import com.example.springtest.api.dto.response.StudentResponse;
import com.example.springtest.domain.Student;
import com.example.springtest.exception.EntityNotFoundException;
import com.example.springtest.repository.LessonRepository;
import com.example.springtest.repository.StudentRepository;
import com.example.springtest.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        clearCache();
        studentRepository.deleteAll();
        lessonRepository.deleteAll();
    }

    @Test
    void create_shouldSaveStudentWithExternalData() {
        StudentCreateRequest request = studentRequest("Bob", "Math");
        UUID studentId = UUID.randomUUID();

        ExternalStudentResponse externalResponse =
                externalResponse(studentId, "extra-info-for-Bob", "bob@mail.com", 20);

        StudentResponse result = studentService.create(request, externalResponse, studentId);

        assertNotNull(result);
        assertEquals(studentId, result.getId());
        assertEquals("Bob", result.getName());
        assertEquals("extra-info-for-Bob", result.getExtra());
        assertEquals("bob@mail.com", result.getEmail());
        assertEquals(20, result.getAge());
        assertNotNull(result.getLessons());
        assertEquals(1, result.getLessons().size());
        assertEquals("Math", result.getLessons().getFirst().getTitle());

        Student savedStudent = studentRepository.findWithLessonsById(studentId).orElseThrow();

        assertEquals("Bob", savedStudent.getName());
        assertEquals("extra-info-for-Bob", savedStudent.getExtra());
        assertEquals("bob@mail.com", savedStudent.getEmail());
        assertEquals(20, savedStudent.getAge());
        assertEquals(1, savedStudent.getLessons().size());
    }

    @Test
    void getById_shouldReturnStudent() {
        UUID studentId = UUID.randomUUID();

        studentService.create(
                studentRequest("Bob", "History"),
                externalResponse(studentId, "saved-extra-info", "bob@mail.com", 20),
                studentId
        );

        StudentResponse result = studentService.getById(studentId);

        assertNotNull(result);
        assertEquals(studentId, result.getId());
        assertEquals("Bob", result.getName());
        assertEquals("saved-extra-info", result.getExtra());
        assertEquals("bob@mail.com", result.getEmail());
        assertEquals(20, result.getAge());
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
        UUID studentId = UUID.randomUUID();

        studentService.create(
                studentRequest("Bob", "Math"),
                externalResponse(studentId, "extra-info", "bob@mail.com", 20),
                studentId
        );

        StudentCreateRequest updateRequest = studentRequest("Alice", "History");

        StudentResponse result = studentService.update(studentId, updateRequest);

        assertNotNull(result);
        assertEquals(studentId, result.getId());
        assertEquals("Alice", result.getName());
        assertEquals("extra-info", result.getExtra());
        assertEquals("bob@mail.com", result.getEmail());
        assertEquals(20, result.getAge());
        assertNotNull(result.getLessons());
        assertEquals(1, result.getLessons().size());
        assertEquals("History", result.getLessons().getFirst().getTitle());

        Student updatedStudent = studentRepository.findWithLessonsById(studentId).orElseThrow();

        assertEquals("Alice", updatedStudent.getName());
        assertEquals("extra-info", updatedStudent.getExtra());
        assertEquals("bob@mail.com", updatedStudent.getEmail());
        assertEquals(20, updatedStudent.getAge());
        assertEquals(1, updatedStudent.getLessons().size());
        assertEquals("History", updatedStudent.getLessons().iterator().next().getTitle());
    }

    @Test
    void update_shouldThrowException_whenStudentNotFound() {
        StudentCreateRequest request = studentRequest("Bob");

        UUID id = UUID.randomUUID();

        assertThrows(EntityNotFoundException.class, () -> studentService.update(id, request));
    }

    @Test
    void delete_shouldRemoveStudent() {
        UUID studentId = UUID.randomUUID();

        studentService.create(
                studentRequest("Bob", "Math"),
                externalResponse(studentId, "extra-info", "bob@mail.com", 20),
                studentId
        );

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
        UUID firstStudentId = UUID.randomUUID();

        studentService.create(
                studentRequest("Bob", "Math"),
                externalResponse(firstStudentId, "extra-1", "bob@mail.com", 20),
                firstStudentId
        );

        UUID secondStudentId = UUID.randomUUID();

        studentService.create(
                studentRequest("Alice", "Math"),
                externalResponse(secondStudentId, "extra-2", "alice@mail.com", 21),
                secondStudentId
        );

        assertEquals(1, lessonRepository.findAll().size());
    }

    @Test
    void getById_shouldUseCache() {
        UUID studentId = UUID.randomUUID();

        studentService.create(
                studentRequest("Bob", "History"),
                externalResponse(studentId, "saved-extra-info", "bob@mail.com", 20),
                studentId
        );

        StudentResponse first = studentService.getById(studentId);

        Student savedStudent = studentRepository.findById(studentId).orElseThrow();
        savedStudent.setName("Changed directly in DB");
        studentRepository.save(savedStudent);

        StudentResponse second = studentService.getById(studentId);

        assertNotNull(first);
        assertNotNull(second);
        assertEquals(studentId, first.getId());
        assertEquals(studentId, second.getId());
        assertEquals("Bob", first.getName());
        assertEquals("Bob", second.getName());
    }

    @Test
    void getById_shouldEvictCacheAfterUpdate() {
        UUID studentId = UUID.randomUUID();

        studentService.create(
                studentRequest("Bob", "History"),
                externalResponse(studentId, "saved-extra-info", "bob@mail.com", 20),
                studentId
        );

        StudentResponse first = studentService.getById(studentId);
        assertNotNull(first);
        assertEquals("Bob", first.getName());

        StudentCreateRequest updateRequest = studentRequest("Alice", "Math");

        studentService.update(studentId, updateRequest);

        StudentResponse second = studentService.getById(studentId);

        assertNotNull(second);
        assertEquals(studentId, second.getId());
        assertEquals("Alice", second.getName());
        assertEquals("saved-extra-info", second.getExtra());
        assertEquals("bob@mail.com", second.getEmail());
        assertEquals(20, second.getAge());
    }

    @Test
    void getById_shouldEvictCacheAfterDelete() {
        UUID studentId = UUID.randomUUID();

        studentService.create(
                studentRequest("Bob", "History"),
                externalResponse(studentId, "saved-extra-info", "bob@mail.com", 20),
                studentId
        );

        StudentResponse cached = studentService.getById(studentId);

        assertNotNull(cached);
        assertEquals(studentId, cached.getId());

        studentService.delete(studentId);

        assertThrows(EntityNotFoundException.class, () -> studentService.getById(studentId));
    }

    private StudentCreateRequest studentRequest(String name, String... lessonTitles) {
        StudentCreateRequest request = new StudentCreateRequest();
        request.setName(name);

        List<LessonCreateRequest> lessons = List.of(lessonTitles)
                .stream()
                .map(title -> {
                    LessonCreateRequest lesson = new LessonCreateRequest();
                    lesson.setTitle(title);
                    return lesson;
                })
                .toList();

        request.setLessons(lessons);

        return request;
    }

    private ExternalStudentResponse externalResponse(UUID studentId, String extraInfo, String email, Integer age) {
        ExternalStudentResponse response = new ExternalStudentResponse();
        response.setStudentId(studentId);
        response.setExtraInfo(extraInfo);
        response.setEmail(email);
        response.setAge(age);
        return response;
    }

    private void clearCache() {
        if (cacheManager.getCache("students") != null) {
            cacheManager.getCache("students").clear();
        }
    }
}