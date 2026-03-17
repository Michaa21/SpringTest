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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

        if (request.getLessonIds() != null && !request.getLessonIds().isEmpty()) {
            Set<Lesson> lessons = getLessonsOrThrow(request.getLessonIds());
            student.setLessons(lessons);
        }

        Student saved = studentRepository.save(student);
        return studentApiMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        return studentApiMapper.toResponse(findStudent(id));
    }

    @Transactional
    public StudentResponse update(Long id, StudentCreateRequest request) {
        Student student = findStudent(id);
        studentApiMapper.update(request, student);

        if (request.getLessonIds() != null) {
            Set<Lesson> lessons = getLessonsOrThrow(request.getLessonIds());
            student.setLessons(lessons);
        }

        Student saved = studentRepository.save(student);
        return studentApiMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        studentRepository.delete(findStudent(id));
    }

    @Transactional
    public Student findStudent(Long id) {
        return studentRepository.findWithLessonsById(id)
                .orElseThrow(() -> {
                    log.warn("Student with id {} not found", id);
                    return new EntityNotFoundException("Student", id);
                });
    }

    public Set<Lesson> getLessonsOrThrow(Iterable<Long> lessonIds) {
        Set<Lesson> lessons = new HashSet<>(lessonRepository.findAllById(lessonIds));

        int requestedSize = ((Collection<?>) lessonIds).size();
        if (lessons.size() != requestedSize) {
            throw new EntityNotFoundException("Lesson", lessonIds);
        }
        return lessons;
    }
}
