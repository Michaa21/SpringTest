package com.example.springtest.service;

import com.example.springtest.client.ExternalStudentClient;
import com.example.springtest.domain.ExternalStudentCompensationTask;
import com.example.springtest.repository.ExternalStudentCompensationTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalStudentCompensationTaskService {

    private final ExternalStudentCompensationTaskRepository compensationTaskRepository;
    private final ExternalStudentClient externalStudentClient;

    @Transactional
    public void createTask(UUID studentId) {
        ExternalStudentCompensationTask task = new ExternalStudentCompensationTask();
        task.setStudentId(studentId);

        compensationTaskRepository.save(task);

        log.info("External student compensation task created. studentId={}", studentId);
    }

    @Transactional
    public void processTasks() {
        List<ExternalStudentCompensationTask> tasks =
                compensationTaskRepository.findUncompletedForUpdate();

        if (tasks.isEmpty()) {
            return;
        }

        log.info("Found {} external student compensation tasks", tasks.size());

        tasks.forEach(this::processTask);
    }

    public void processTask(ExternalStudentCompensationTask task) {
        try {
            externalStudentClient.compensateStudentCreation(task.getStudentId());

            task.setCompleted(true);
            task.setCompletedAt(OffsetDateTime.now());
            task.setAttempts(task.getAttempts() + 1);

            compensationTaskRepository.save(task);

            log.info(
                    "External student compensation task completed. studentId={}",
                    task.getStudentId()
            );
        } catch (Exception ex) {
            task.setAttempts(task.getAttempts() + 1);
            compensationTaskRepository.save(task);

            log.error(
                    "Failed to process external student compensation task. studentId={}, attempts={}",
                    task.getStudentId(),
                    task.getAttempts(),
                    ex
            );
        }
    }
}