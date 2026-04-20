package com.example.springtest.service;

import com.example.springtest.api.dto.request.StudentCreateRequest;
import com.example.springtest.api.dto.response.StudentResponse;
import com.example.springtest.domain.Lesson;
import com.example.springtest.domain.Student;
import com.example.springtest.exception.EntityNotFoundException;
import com.example.springtest.mapper.StudentApiMapper;
import com.example.springtest.repository.LessonRepository;
import com.example.springtest.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentApiMapper studentApiMapper;
    private final ExternalServiceCaller externalServiceCaller;
    private final LessonRepository lessonRepository;

    @Transactional
    public StudentResponse create(StudentCreateRequest request, String extraInfo) {

        Student student = studentApiMapper.toEntity(request);
        student.setExtra(extraInfo);
        Set<Lesson> lessons = request.getLessons()
                .stream()
                .map(lessonRequest -> {
                    String title = lessonRequest.getTitle().trim();

                    return lessonRepository.findByTitleIgnoreCase(title)
                            .orElseGet(() -> new Lesson(title));
                })
                .collect(Collectors.toSet());

        student.setLessons(lessons);

        Student saved = studentRepository.save(student);

        return studentApiMapper.toResponse(saved);
    }

    @Cacheable(value = "students", key = "#id")
    @Transactional(readOnly = true)
    public StudentResponse getById(UUID id) {
        Student student = findStudent(id);

        String extra = externalServiceCaller.getExtra(id);
        StudentResponse response = studentApiMapper.toResponse(student);
        response.setExtra(extra);

        return response;
    }

    @CachePut(value = "students", key = "#id")
    @Transactional
    public StudentResponse update(UUID id, StudentCreateRequest request) {
        Student student = findStudent(id);

        studentApiMapper.update(request, student);

        Set<Lesson> lessons = request.getLessons()
                .stream()
                .map(lessonRequest -> new Lesson(lessonRequest.getTitle()))
                .collect(Collectors.toSet());

        student.setLessons(lessons);

        return studentApiMapper.toResponse(student);
    }

    @CacheEvict(value = "students", key = "#id")
    @Transactional
    public void delete(UUID id) {
        studentRepository.delete(findStudent(id));
    }

    @Transactional
    public Student findStudent(UUID id) {
        return studentRepository.findWithLessonsById(id)
                .orElseThrow(() -> {
                    log.warn("Student with id {} not found", id);
                    return new EntityNotFoundException("Student", id);
                });
    }
}
