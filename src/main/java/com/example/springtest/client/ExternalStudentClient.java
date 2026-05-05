package com.example.springtest.client;

import com.example.springtest.api.dto.request.ExternalStudentRequest;
import com.example.springtest.api.dto.response.ExternalStudentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "external-service", url = "${external-service.url}")
public interface ExternalStudentClient {

    @Retry(name = "externalService")
    @CircuitBreaker(name = "externalService")
    @GetMapping("/external/students/{studentId}")
    ExternalStudentResponse getStudentExtraInfo(@PathVariable String studentId);

    @CircuitBreaker(name = "externalService")
    @PostMapping("/external/students")
    ExternalStudentResponse createExternalStudent(@RequestBody ExternalStudentRequest request);

    @Retry(name = "externalService")
    @CircuitBreaker(name = "externalService")
    @DeleteMapping("/external/students/{studentId}/compensation")
    void compensateStudentCreation(@PathVariable UUID studentId);}
