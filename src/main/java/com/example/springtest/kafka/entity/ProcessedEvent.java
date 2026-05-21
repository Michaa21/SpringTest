package com.example.springtest.kafka.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "processed_events", schema = "spring_test")
public class ProcessedEvent {

    @Id
    private UUID eventId;

    private OffsetDateTime processedAt;

    public ProcessedEvent() {
    }

    public ProcessedEvent(UUID eventId, OffsetDateTime processedAt) {
        this.eventId = eventId;
        this.processedAt = processedAt;
    }

    public UUID getEventId() {
        return eventId;
    }

    public OffsetDateTime getProcessedAt() {
        return processedAt;
    }
}