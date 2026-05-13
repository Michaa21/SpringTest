package com.example.springtest.scheduler;

import com.example.springtest.service.ExternalStudentCompensationTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "external-student-compensation.scheduler.enabled",
        havingValue = "true"
)
public class ExternalStudentCompensationScheduler {

    private final ExternalStudentCompensationTaskService compensationTaskService;

    @Scheduled(fixedDelayString = "${external-student-compensation.scheduler.fixed-delay-ms}")
    public void processCompensationTasks() {
        compensationTaskService.processTasks();
    }
}