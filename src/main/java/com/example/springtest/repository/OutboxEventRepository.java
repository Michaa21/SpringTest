package com.example.springtest.repository;

import com.example.springtest.domain.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
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

    @Modifying
    @Query(value = """
            update spring_test.outbox_events
            set status = 'NEW',
                processing_started_at = null
            where status = 'PROCESSING'
            and processing_started_at < :timeout
            """, nativeQuery = true)
    int resetStuckProcessingEvents(@Param("timeout") OffsetDateTime timeout);

    @Modifying
    @Query(value = """
            update spring_test.outbox_events
            set status = 'PUBLISHED',
                published_at = :publishedAt,
                processing_started_at = null,
                last_error = null
            where id = :id
            and status = 'PROCESSING'
            """, nativeQuery = true)
    int markAsPublishedIfProcessing(
            @Param("id") UUID id,
            @Param("publishedAt") OffsetDateTime publishedAt
    );
}
