package com.example.springtest.kafka.service;

import com.example.springtest.kafka.entity.ProcessedEvent;
import com.example.springtest.kafka.event.ExternalStudentCreatedEvent;
import com.example.springtest.kafka.repository.ProcessedEventRepository;
import com.example.springtest.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class ExternalStudentCreatedEventHandler {

    private final StudentRepository studentRepository;
    private final ProcessedEventRepository processedEventRepository;

    public ExternalStudentCreatedEventHandler(
            StudentRepository studentRepository,
            ProcessedEventRepository processedEventRepository
    ) {
        this.studentRepository = studentRepository;
        this.processedEventRepository = processedEventRepository;
    }

    @Transactional
    public void handle(ExternalStudentCreatedEvent event) {
        if (processedEventRepository.existsById(event.eventId())) {
            return;
        }

        var student = studentRepository.findById(event.studentId())
                .orElseThrow(() -> new IllegalStateException(
                        "Student not found by id: " + event.studentId()
                ));

        student.setExtra(event.extraInfo());

        processedEventRepository.save(
                new ProcessedEvent(event.eventId(), OffsetDateTime.now())
        );
    }
}