package com.example.springtest.service;

import com.example.springtest.model.Student;
import com.example.springtest.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Student crateStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Студент с id " + id +
                        " не найден"));
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student student = getById(id);

        if (studentDetails.getName() != null) {
            student.setName(studentDetails.getName());
        }
        if (studentDetails.getLessons() != null) {
            student.getLessons().clear();
            student.getLessons().addAll(studentDetails.getLessons());
        }
        return student;
    }

    public void deleteStudent(Long id) {
        studentRepository.delete(getById(id));
    }
}
