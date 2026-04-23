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
    public String getExtra(UUID studentId) {
        log.info("Calling external service studentId={}", studentId);

        String extra = externalStudentClient
                .getStudentExtraInfo(studentId.toString())
                .getExtraInfo();

        log.info("Received extra={} for studentId={}", extra, studentId);

        return extra;
    }

    @CircuitBreaker(name = "externalService", fallbackMethod = "createExternalStudentFallback")
    public ExternalStudentResponse createExternalStudent(ExternalStudentRequest request) {
        log.info("Calling external service POST with studentId={} and name={}",
                request.getStudentId(), request.getName());

        ExternalStudentResponse response = externalStudentClient.createExternalStudent(request);

        log.info("Received external response: studentId={}, extraInfo={}",
                response.getStudentId(), response.getExtraInfo());
        return response;
    }

    public String getExtraFallback(UUID studentId, Throwable ex) {
        log.warn("Fallback triggered for studentId={} due to {}", studentId, ex.toString());
        return "no extra info";
    }

    @Retry(name = "externalService")
    @CircuitBreaker(name = "externalService", fallbackMethod = "deleteExternalStudentFallback")
    public void deleteExternalStudent(UUID studentId) {
        log.info("Calling external service DELETE studentId={}", studentId);

        externalStudentClient.deleteExternalStudent(studentId);

        log.info("External student deleted studentId={}", studentId);
    }

    public ExternalStudentResponse createExternalStudentFallback(ExternalStudentRequest request, Throwable ex) {
        log.warn("Fallback triggered for POST studentId={} and name={} due to {}",
                request.getStudentId(), request.getName(), ex.toString());

        ExternalStudentResponse response = new ExternalStudentResponse();
        response.setExtraInfo("no extra info");

        return response;
    }

    public void deleteExternalStudentFallback(UUID studentId, Throwable ex) {
        log.warn("Fallback triggered for DELETE studentId={} due to {}", studentId, ex.toString());
    }
}
