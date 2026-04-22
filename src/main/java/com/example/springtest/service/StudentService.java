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
import org.springframework.transaction.support.TransactionTemplate;

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
    private final TransactionTemplate transactionTemplate;

    public StudentResponse create(StudentCreateRequest request, String extraInfo) {
        return transactionTemplate.execute(status -> {
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
        });
    }

    @Cacheable(value = "students", key = "#id")
    public StudentResponse getById(UUID id) {
        StudentResponse response = transactionTemplate.execute(status -> {
            Student student = studentRepository.findWithLessonsById(id)
                    .orElseThrow(() -> {
                        log.warn("Student with id {} not found", id);
                        return new EntityNotFoundException("Student", id);
                    });

            return studentApiMapper.toResponse(student);
        });

        String extra = externalServiceCaller.getExtra(id);
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
