package com.example.springtest.scheduler;

import com.example.springtest.service.ExternalStudentCompensationTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExternalStudentCompensationScheduler {

    private final ExternalStudentCompensationTaskService compensationTaskService;

    @Value("${external-student-compensation.scheduler.enabled}")
    private boolean enabled;

    @Scheduled(fixedDelayString = "${external-student-compensation.scheduler.fixed-delay-ms}")
    public void processCompensationTasks() {
        if (!enabled) {
            return;
        }

        compensationTaskService.processTasks();
    }
}