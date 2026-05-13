package com.example.springtest.repository;

import com.example.springtest.domain.ExternalStudentCompensationTask;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ExternalStudentCompensationTaskRepository
        extends JpaRepository<ExternalStudentCompensationTask, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select task
            from ExternalStudentCompensationTask task
            where task.completed = false 
            """)
    List<ExternalStudentCompensationTask> findUncompletedForUpdate();
}