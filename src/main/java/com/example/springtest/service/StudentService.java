package com.example.springtest.service;

import com.example.springtest.api.dto.request.LessonCreateRequest;
import com.example.springtest.api.dto.request.StudentCreateRequest;
import com.example.springtest.api.dto.response.StudentResponse;
import com.example.springtest.exception.EntityNotFoundException;
import com.example.springtest.mapper.StudentApiMapper;
import com.example.springtest.domain.Lesson;
import com.example.springtest.domain.Student;
import com.example.springtest.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentApiMapper studentApiMapper;

    @Transactional
    public StudentResponse create(StudentCreateRequest request) {
        Student student = studentApiMapper.toEntity(request);
        Student saved = studentRepository.save(student);
        return studentApiMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        return studentApiMapper.toResponse(findStudent(id));
    }

    @Transactional
    public StudentResponse update(Long id, StudentCreateRequest request) {
        Student student = findStudent(id);
        student.setName(request.getName());
        student.getLessons().clear();
        for (LessonCreateRequest lessonDto : request.getLessons()) {
            Lesson lesson = new Lesson();
            lesson.setTitle(lessonDto.getTitle());
            student.getLessons().add(lesson);
        }
        Student saved = studentRepository.save(student);
        return studentApiMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        studentRepository.delete(findStudent(id));
    }

    public Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student", id));
    }
}
