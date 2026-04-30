package com.example.springtest.controller;

import com.example.springtest.api.dto.response.StudentResponse;
import com.example.springtest.exception.EntityNotFoundException;
import com.example.springtest.service.StudentSagaOrchestratorService;
import com.example.springtest.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StudentService studentService;

    @MockBean
    StudentSagaOrchestratorService studentSagaOrchestratorService;

    @Test
    void createStudent_shouldReturn200() throws Exception {
        StudentResponse response = new StudentResponse();
        response.setId(UUID.randomUUID().toString());
        response.setName("Bob");
        response.setExtra("extra-info-for-Bob");

        when(studentSagaOrchestratorService.createStudent(any())).thenReturn(response);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Bob",
                                  "email": "bob@mail.com",
                                  "age": 18,
                                  "lessons": [
                                    {
                                      "title": "Math"
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.extra").value("extra-info-for-Bob"));
    }

    @Test
    void getStudentById_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();

        StudentResponse response = new StudentResponse();
        response.setId(id.toString());
        response.setName("Bob");
        response.setExtra("extra-info-for-" + id);

        when(studentService.getById(id)).thenReturn(response);

        mockMvc.perform(get("/api/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.extra").value("extra-info-for-" + id));
    }

    @Test
    void getStudentById_shouldReturn404_whenStudentNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(studentService.getById(id)).thenThrow(new EntityNotFoundException("Student", id));

        mockMvc.perform(get("/api/students/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateStudent_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();

        StudentResponse response = new StudentResponse();
        response.setId(id.toString());
        response.setName("Updated Bob");
        response.setExtra("extra-info");

        when(studentService.update(eq(id), any())).thenReturn(response);

        mockMvc.perform(put("/api/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Bob",
                                  "email": "bob@mail.com",
                                  "age": 18,
                                  "lessons": [
                                    {
                                      "title": "History"
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Updated Bob"))
                .andExpect(jsonPath("$.extra").value("extra-info"));
    }

    @Test
    void deleteStudent_shouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(studentService).delete(id);

        mockMvc.perform(delete("/api/students/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void createStudent_shouldReturn400_whenNameIsNull() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "lessons": []
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createStudent_shouldReturn400_whenLessonsIsExplicitlyNull() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Bob",
                                  "lessons": null
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}