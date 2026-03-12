package com.example.springtest.repository;

import com.example.springtest.domain.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @EntityGraph(attributePaths = "lessons")
    Optional<Student> findWithLessonsById(Long id);
}
