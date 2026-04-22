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

    @CircuitBreaker(name = "externalService", fallbackMethod = "createExternalStudentFallback")
    public ExternalStudentResponse createExternalStudent(ExternalStudentRequest request) {
        log.info("Calling external service POST with extraInfo={}", request.getExtraInfo());

        ExternalStudentResponse response = externalStudentClient.createExternalStudent(request);

        log.info("Received external response: id={}, extraInfo={}",response.getId(), response.getExtraInfo());

        return response;
    }

    public String getExtraFallback(UUID id, Throwable ex) {
        log.warn("Fallback triggered for id={} due to {}", id, ex.toString());
        return "no extra info";
    }

    @Retry(name = "externalService")
    @CircuitBreaker(name = "externalService", fallbackMethod = "deleteExternalStudentFallback")
    public void deleteExternalStudent(UUID id) {
        log.info("Calling external service DELETE id={}", id);

        externalStudentClient.deleteExternalStudent(id);

        log.info("External student deleted id={}", id);
    }

    public ExternalStudentResponse createExternalStudentFallback(ExternalStudentRequest request, Throwable ex) {
        log.warn("Fallback triggered for POST extraInfo={} due to {}", request.getExtraInfo(), ex.toString());

        ExternalStudentResponse response = new ExternalStudentResponse();
        response.setExtraInfo("no extra info");

        return response;
    }

    public void deleteExternalStudentFallback(UUID id, Throwable ex) {
        log.warn("Fallback triggered for DELETE id={} due to {}", id, ex.toString());
    }
}
