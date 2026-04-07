package com.example.springtest.client;

import com.example.springtest.api.dto.response.ExternalStudentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "external-service", url = "http://localhost:8081")
public interface ExternalStudentClient {

    @GetMapping("/external/students/{id}")
    ExternalStudentResponse getStudentExtraInfo(@PathVariable String id);
}
