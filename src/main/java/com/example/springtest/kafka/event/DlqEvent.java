package com.example.springtest.kafka.event;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DlqEvent(
        UUID eventId,
        String sourceTopic,
        String originalPayload,
        String errorMessage,
        OffsetDateTime failedAt
) {
}