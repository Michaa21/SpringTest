package com.example.springtest.service;

import com.example.springtest.api.model.LessonCreateRequest;
import com.example.springtest.api.model.StudentCreateRequest;
import com.example.springtest.api.model.StudentResponse;
import com.example.springtest.exception.StudentNotFoundException;
import com.example.springtest.mapper.StudentApiMapper;
import com.example.springtest.model.Lesson;
import com.example.springtest.model.Student;
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

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }
}
