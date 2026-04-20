package com.example.springtest.client;

import com.example.springtest.api.dto.request.ExternalStudentRequest;
import com.example.springtest.api.dto.response.ExternalStudentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "external-service", url = "http://localhost:8081")
public interface ExternalStudentClient {

    @GetMapping("/external/students/{id}")
    ExternalStudentResponse getStudentExtraInfo(@PathVariable String id);

    @PostMapping("/external/students")
    ExternalStudentResponse createExternalStudent(@RequestBody ExternalStudentRequest request);

    @DeleteMapping("/external/students/{id}")
    void deleteExternalStudent(@PathVariable UUID id);
}
