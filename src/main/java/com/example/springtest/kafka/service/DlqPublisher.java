package com.example.springtest.kafka.service;

import com.example.springtest.kafka.KafkaTopics;
import com.example.springtest.kafka.event.DlqEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class DlqPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(DlqPublisher.class);

    public DlqPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(String sourceTopic, String originalPayload, Exception exception) {
        try {
            DlqEvent dlqEvent = new DlqEvent(
                    UUID.randomUUID(),
                    sourceTopic,
                    originalPayload,
                    exception.getClass().getSimpleName() + ": " + exception.getMessage(),
                    OffsetDateTime.now()
            );

            String payload = objectMapper.writeValueAsString(dlqEvent);

            kafkaTemplate.send(KafkaTopics.DLQ_EVENTS, dlqEvent.eventId().toString(), payload)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish DLQ event", ex);
                        } else {
                            log.info("DLQ event published to topic {} with key {}",
                                    KafkaTopics.DLQ_EVENTS,
                                    dlqEvent.eventId());
                        }
                    });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize DLQ event", e);
        }
    }
}