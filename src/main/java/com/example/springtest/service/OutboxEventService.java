package com.example.springtest.service;

import com.example.springtest.domain.OutboxEvent;
import com.example.springtest.domain.OutboxEventStatus;
import com.example.springtest.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxEventService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public void createOutboxEvent(
            UUID eventId,
            String aggregateType,
            UUID aggregateId,
            String eventType,
            String topic,
            Object payloadObject
    ) {
        String payload = toJson(payloadObject);

        OutboxEvent outboxEvent = toOutboxEvent(
                eventId,
                aggregateType,
                aggregateId,
                eventType,
                topic,
                payload
        );

        outboxEventRepository.save(outboxEvent);
    }

    private OutboxEvent toOutboxEvent(
            UUID eventId,
            String aggregateType,
            UUID aggregateId,
            String eventType,
            String topic,
            String payload
    ) {
        OutboxEvent outboxEvent = new OutboxEvent();

        outboxEvent.setId(UUID.randomUUID());
        outboxEvent.setEventId(eventId);
        outboxEvent.setAggregateType(aggregateType);
        outboxEvent.setAggregateId(aggregateId);
        outboxEvent.setEventType(eventType);
        outboxEvent.setTopic(topic);
        outboxEvent.setPayload(payload);
        outboxEvent.setStatus(OutboxEventStatus.NEW);

        return outboxEvent;
    }

    private String toJson(Object payloadObject) {
        try {
            return objectMapper.writeValueAsString(payloadObject);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to serialize outbox event payload", exception);
        }
    }
}
