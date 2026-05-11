package com.example.springtest.scheduler;

import com.example.springtest.domain.ExternalStudentCompensationTask;
import com.example.springtest.repository.ExternalStudentCompensationTaskRepository;
import com.example.springtest.service.ExternalStudentCompensationTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "external-student-compensation.scheduler.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class ExternalStudentCompensationScheduler {

    private final ExternalStudentCompensationTaskRepository compensationTaskRepository;
    private final ExternalStudentCompensationTaskService compensationTaskService;

    @Scheduled(fixedDelayString = "${external-student-compensation.fixed-delay-ms:10000}")
    public void processCompensationTasks() {
        List<ExternalStudentCompensationTask> tasks =
                compensationTaskRepository.findByCompletedFalse();

        if (tasks.isEmpty()) {
            return;
        }

        log.info("Found {} external student compensation tasks", tasks.size());

        tasks.forEach(compensationTaskService::processTask);
    }
}