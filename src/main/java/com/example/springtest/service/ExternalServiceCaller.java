package com.example.springtest.service;

import com.example.springtest.client.ExternalStudentClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalServiceCaller {

    private final ExternalStudentClient externalStudentClient;

    @CircuitBreaker(name = "externalService", fallbackMethod = "getExtraFallback")
    @Retryable(
            backoff = @Backoff(
                    delay = 500,
                    multiplier = 2,
                    random = true
            )
    )
    public String getExtra(UUID id) {
        log.info("Calling external service for id={}", id);
        return externalStudentClient.getStudentExtraInfo(id.toString());
    }

    public String getExtraFallback(UUID id, Exception ex) {
        log.warn("Fallback triggered for id={} due to {}", id, ex.getMessage());
        return "no extra info";
    }
}
