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
    private final LessonRepository lessonRepository;

    @Transactional
    public StudentResponse create(StudentCreateRequest request) {
        Student student = studentApiMapper.toEntity(request);

        if (request.getLessons() != null && !request.getLessons().isEmpty()) {
            Set<Lesson> lessons = request.getLessons()
                    .stream()
                    .map(lr -> getOrCreateLesson(lr.getTitle()))
                    .collect(Collectors.toSet());

            student.setLessons(lessons);
        }

        Student saved = studentRepository.save(student);
        return studentApiMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public StudentResponse getById(UUID id) {
        return studentApiMapper.toResponse(findStudent(id));
    }

    @Transactional
    public StudentResponse update(UUID id, StudentCreateRequest request) {
        Student student = findStudent(id);

        studentApiMapper.update(request, student);

        if (request.getLessons() != null) {
            Set<Lesson> lessons = request.getLessons()
                    .stream()
                    .map(lr -> getOrCreateLesson(lr.getTitle()))
                    .collect(Collectors.toSet());

            student.setLessons(lessons);
        }

        Student saved = studentRepository.save(student);
        return studentApiMapper.toResponse(saved);
    }

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

    private Lesson getOrCreateLesson(String title) {
        String normalized = title.trim().toLowerCase();
        return lessonRepository.findByTitleIgnoreCase(normalized)
                .orElseGet(() -> lessonRepository.save(new Lesson(normalized)));
    }
}
