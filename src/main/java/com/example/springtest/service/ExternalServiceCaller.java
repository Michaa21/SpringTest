package com.example.springtest.service;

import com.example.springtest.client.ExternalStudentClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalServiceCaller {

    private final ExternalStudentClient externalStudentClient;

    @Retry(name = "externalService")
    @CircuitBreaker(name = "externalService", fallbackMethod = "getExtraFallback")
    public String getExtra(UUID id) {
        log.info("Calling external service id={}", id);

        String extra = externalStudentClient
                .getStudentExtraInfo(id.toString())
                .getExtraInfo();

        log.info("Received extra = {}", extra);

        return extra;
    }

    public String getExtraFallback(UUID id, Throwable ex) {
        log.warn("Fallback triggered for id={} due to {}", id, ex.toString());
        return "no extra info";
    }
}
