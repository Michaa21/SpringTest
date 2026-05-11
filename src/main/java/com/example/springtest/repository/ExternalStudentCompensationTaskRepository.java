package com.example.springtest.repository;

import com.example.springtest.domain.ExternalStudentCompensationTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExternalStudentCompensationTaskRepository
        extends JpaRepository<ExternalStudentCompensationTask, UUID> {

    List<ExternalStudentCompensationTask> findByCompletedFalse();
}