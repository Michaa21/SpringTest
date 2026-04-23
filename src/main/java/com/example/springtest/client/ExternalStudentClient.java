package com.example.springtest.client;

import com.example.springtest.api.dto.request.ExternalStudentRequest;
import com.example.springtest.api.dto.response.ExternalStudentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "external-service", url = "${external-service.url}")
public interface ExternalStudentClient {

    @GetMapping("/external/students/{studentId}")
    ExternalStudentResponse getStudentExtraInfo(@PathVariable String studentId);

    @PostMapping("/external/students")
    ExternalStudentResponse createExternalStudent(@RequestBody ExternalStudentRequest request);

    @DeleteMapping("/external/students/{studentId}")
    void deleteExternalStudent(@PathVariable UUID studentId);
}
