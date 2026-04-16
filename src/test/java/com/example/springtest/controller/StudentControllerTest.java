package com.example.springtest.controller;

import com.example.springtest.api.dto.response.StudentResponse;
import com.example.springtest.exception.EntityNotFoundException;
import com.example.springtest.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    void getStudentById_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();

        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setId(id.toString());
        studentResponse.setName("Bob");
        studentResponse.setExtra("extra-info-for-" + id);

        when(studentService.getById(id)).thenReturn(studentResponse);

        mockMvc.perform(get("/api/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.extra").value("extra-info-for-" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getStudentById_shouldReturn404_whenNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(studentService.getById(id)).thenThrow(new EntityNotFoundException("Student", id));

        mockMvc.perform(get("/api/students/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void createStudent_shouldReturn200() throws Exception {
        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setId(UUID.randomUUID().toString());
        studentResponse.setName("Misha");
        studentResponse.setExtra("extra-info-for-Misha");

        when(studentService.create(any())).thenReturn(studentResponse);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Misha",
                                  "lessons": []
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(studentResponse.getId()))
                .andExpect(jsonPath("$.name").value("Misha"))
                .andExpect(jsonPath("$.extra").value("extra-info-for-Misha"));
    }
}

