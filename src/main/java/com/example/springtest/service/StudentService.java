package com.example.springtest.service;

import com.example.springtest.api.dto.request.StudentCreateRequest;
import com.example.springtest.api.dto.response.ExternalStudentResponse;
import com.example.springtest.api.dto.response.StudentResponse;
import com.example.springtest.domain.Lesson;
import com.example.springtest.domain.Student;
import com.example.springtest.exception.EntityNotFoundException;
import com.example.springtest.kafka.KafkaTopics;
import com.example.springtest.kafka.event.StudentCreateRequestedEvent;
import com.example.springtest.mapper.StudentApiMapper;
import com.example.springtest.outbox.OutboxEventService;
import com.example.springtest.repository.LessonRepository;
import com.example.springtest.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.OffsetDateTime;
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
    private final TransactionTemplate transactionTemplate;
    private final OutboxEventService outboxEventService;

    public StudentResponse create(StudentCreateRequest request, ExternalStudentResponse externalResponse, UUID studentId) {
        return transactionTemplate.execute(status -> {
            Student student = studentApiMapper.toEntity(request);
            student.setId(studentId);

            studentApiMapper.updateFromExternalResponse(externalResponse, student);

            student.setLessons(resolveLessons(request));

            Student saved = studentRepository.save(student);
            log.info("Student with id {} created", saved.getId());

            UUID eventId = UUID.randomUUID();

            StudentCreateRequestedEvent event = new StudentCreateRequestedEvent(
                    eventId,
                    saved.getId(),
                    saved.getName(),
                    saved.getEmail(),
                    saved.getAge(),
                    saved.getLessons()
                            .stream()
                            .map(Lesson::getTitle)
                            .toList(),
                    OffsetDateTime.now()
            );

            outboxEventService.createOutboxEvent(
                    eventId,
                    "STUDENT",
                    saved.getId(),
                    "STUDENT_CREATE_REQUESTED",
                    KafkaTopics.STUDENT_CREATE_REQUESTS,
                    event
            );

            return studentApiMapper.toResponse(saved);
        });
    }

    public StudentResponse create(StudentCreateRequest request) {
        return transactionTemplate.execute(status -> {
            Student student = studentApiMapper.toEntity(request);
            student.setId(UUID.randomUUID());
            student.setName(request.getName());
            student.setEmail(request.getEmail());
            student.setAge(request.getAge());
            student.setLessons(resolveLessons(request));

            Student saved = studentRepository.save(student);
            log.info("Student with id {} created", saved.getId());

            UUID eventId = UUID.randomUUID();

            StudentCreateRequestedEvent event = new StudentCreateRequestedEvent(
                    eventId,
                    saved.getId(),
                    saved.getName(),
                    saved.getEmail(),
                    saved.getAge(),
                    saved.getLessons()
                            .stream()
                            .map(Lesson::getTitle)
                            .toList(),
                    OffsetDateTime.now()
            );

            outboxEventService.createOutboxEvent(
                    eventId,
                    "STUDENT",
                    saved.getId(),
                    "STUDENT_CREATE_REQUESTED",
                    KafkaTopics.STUDENT_CREATE_REQUESTS,
                    event
            );

            return studentApiMapper.toResponse(saved);
        });
    }

    @Cacheable(value = "students", key = "#id")
    public StudentResponse getById(UUID id) {
        return transactionTemplate.execute(status -> {
            Student student = studentRepository.findWithLessonsById(id)
                    .orElseThrow(() -> {
                        log.warn("Student with id {} not found", id);
                        return new EntityNotFoundException("Student", id);
                    });

            return studentApiMapper.toResponse(student);
        });
    }

    @CacheEvict(value = "students", key = "#id")
    @Transactional
    public StudentResponse update(UUID id, StudentCreateRequest request) {
        Student student = findStudent(id);

        studentApiMapper.update(request, student);
        student.setLessons(resolveLessons(request));

        StudentResponse response = studentApiMapper.toResponse(student);
        log.info("Student with id {} updated", id);

        return response;
    }

    @CacheEvict(value = "students", key = "#id")
    @Transactional
    public void delete(UUID id) {
        studentRepository.delete(findStudent(id));
        log.info("Student with id {} deleted", id);
    }

    @Transactional(readOnly = true)
    public Student findStudent(UUID id) {
        return studentRepository.findWithLessonsById(id)
                .orElseThrow(() -> {
                    log.warn("Student with id {} not found", id);
                    return new EntityNotFoundException("Student", id);
                });
    }

    private Set<Lesson> resolveLessons(StudentCreateRequest request) {
        return request.getLessons()
                .stream()
                .map(lessonRequest -> {
                    String title = lessonRequest.getTitle().trim();

                    return lessonRepository.findByTitleIgnoreCase(title)
                            .orElseGet(() -> lessonRepository.save(new Lesson(title)));
                })
                .collect(Collectors.toSet());
    }
}