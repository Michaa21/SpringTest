package com.example.springtest.controller;

import com.example.springtest.model.Student;
import com.example.springtest.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public Student create(@RequestBody Student student){
        return studentService.crateStudent(student);
    }

    @GetMapping("/{id}")
    public Student get(@PathVariable Long id){
        return studentService.getById(id);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student student){
        return studentService.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        studentService.deleteStudent(id);
    }
}
