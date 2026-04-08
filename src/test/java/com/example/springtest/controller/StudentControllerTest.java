package com.example.springtest.controller;

import com.example.springtest.api.dto.response.StudentResponse;
import com.example.springtest.exception.EntityNotFoundException;
import com.example.springtest.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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

        when(studentService.getById(id)).thenReturn(studentResponse);

        mockMvc.perform(get("/api/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getStudentById_shouldReturn404_whenNotFound() throws Exception{
        UUID id = UUID.randomUUID();

        when(studentService.getById(id)).thenThrow(new EntityNotFoundException("Student", id));

        mockMvc.perform(get("/api/students/{id}", id))
                .andExpect(status().isNotFound());
    }
}
