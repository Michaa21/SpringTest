package com.example.springtest.repository;

import com.example.springtest.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    Optional<Lesson> findByTitleIgnoreCase(String title);
}
