package com.example.springtest.repository;

import com.example.springtest.domain.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    @Query(value = """
            select *
            from spring_test.outbox_events
            where status = :status
            order by created_at asc
            limit 10
            for update skip locked
            """, nativeQuery = true)
    List<OutboxEvent> findTop10ByStatusForUpdateSkipLocked(String status);
}
