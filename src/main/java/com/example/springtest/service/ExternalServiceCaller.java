package com.example.springtest.service;

import com.example.springtest.api.dto.request.ExternalStudentRequest;
import com.example.springtest.api.dto.response.ExternalStudentResponse;
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

        log.info("Received extra={} for id={}", extra, id);

        return extra;
    }

    @Retry(name = "externalService")
    @CircuitBreaker(name = "externalService", fallbackMethod = "createExternalStudentFallback")
    public ExternalStudentResponse createExternalStudent(String extraInfo) {
        log.info("Calling external service POST with extraInfo={}", extraInfo);

        ExternalStudentRequest request = new ExternalStudentRequest(extraInfo);
        ExternalStudentResponse response = externalStudentClient.createExternalStudent(request);

        log.info("Received external response: extraInfo={}", response.getExtraInfo());

        return response;
    }

    public String getExtraFallback(UUID id, Throwable ex) {
        log.warn("Fallback triggered for id={} due to {}", id, ex.toString());
        return "no extra info";
    }

    public ExternalStudentResponse createExternalStudentFallback(String extraInfo, Throwable ex) {
        log.warn("Fallback triggered for POST extraInfo={} due to {}", extraInfo, ex.toString());

        ExternalStudentResponse response = new ExternalStudentResponse();
        response.setExtraInfo("no extra info");

        return response;
    }
}
