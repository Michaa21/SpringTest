package com.example.springtest.kafka.consumer;

import com.example.springtest.kafka.KafkaTopics;
import com.example.springtest.kafka.event.ExternalStudentCreatedEvent;
import com.example.springtest.kafka.service.DlqPublisher;
import com.example.springtest.kafka.service.ExternalStudentCreatedEventHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class ExternalStudentCreatedConsumer {

    private final ObjectMapper objectMapper;
    private final ExternalStudentCreatedEventHandler eventHandler;
    private final DlqPublisher dlqPublisher;
    private static final Logger log = LoggerFactory.getLogger(ExternalStudentCreatedConsumer.class);

    public ExternalStudentCreatedConsumer(
            ObjectMapper objectMapper,
            ExternalStudentCreatedEventHandler eventHandler,
            DlqPublisher dlqPublisher
    ) {
        this.objectMapper = objectMapper;
        this.eventHandler = eventHandler;
        this.dlqPublisher = dlqPublisher;
    }

    @KafkaListener(
            topics = KafkaTopics.EXTERNAL_STUDENT_CREATED_EVENTS,
            groupId = "spring-test")

    public void consume(String payload, Acknowledgment acknowledgment) {

        try {
            ExternalStudentCreatedEvent event = objectMapper.readValue(
                    payload,
                    ExternalStudentCreatedEvent.class
            );

            eventHandler.handle(event);

            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Failed to process ExternalStudentCreatedEvent, sending to DLQ. Payload: {}", payload, e);
            dlqPublisher.publish(
                    KafkaTopics.EXTERNAL_STUDENT_CREATED_EVENTS,
                    payload,
                    e
            );

            acknowledgment.acknowledge();
        }
    }
}