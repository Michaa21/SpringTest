package com.example.springtest.kafka;

import com.example.springtest.config.OutboxPublisherProperties;
import com.example.springtest.domain.OutboxEvent;
import com.example.springtest.domain.OutboxEventStatus;
import com.example.springtest.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final TransactionTemplate transactionTemplate;
    private final OutboxPublisherProperties properties;

    @Scheduled(fixedDelay = 5000)
    public void publishNewEvents() {
        List<OutboxEvent> events = fetchAndMarkProcessing();

        for (OutboxEvent event : events) {
            publishOne(event);
        }
    }

    private List<OutboxEvent> fetchAndMarkProcessing() {
        return transactionTemplate.execute(status -> {
            List<OutboxEvent> events = outboxEventRepository
                    .findTop10ByStatusForUpdateSkipLocked(OutboxEventStatus.NEW.name());

            for (OutboxEvent event : events) {
                event.setStatus(OutboxEventStatus.PROCESSING);
            }
            return events;
        });
    }

    private void publishOne(OutboxEvent event) {

        try {
            kafkaTemplate.send(
                    event.getTopic(),
                    event.getAggregateId().toString(),
                    event.getPayload()
            ).get();
        } catch (Exception exception) {
            markAsFailedAttempt(event.getId(), exception.getMessage());
            log.error("Failed to publish outbox event {}", event.getEventId(), exception);
            return;
        }
        try {
            markAsPublished(event.getId());

            log.info("Outbox event {} published to topic {}",
                    event.getEventId(), event.getTopic());
        } catch (Exception exception) {
            log.error("Outbox event {} was sent to Kafka, but status update failed",
                    event.getEventId(), exception);
        }
    }

    private void markAsPublished(UUID eventId) {
        transactionTemplate.executeWithoutResult(status -> {
            OutboxEvent event = outboxEventRepository.findById(eventId)
                    .orElseThrow();
            event.setStatus(OutboxEventStatus.PUBLISHED);
            event.setPublishedAt(OffsetDateTime.now());
            event.setLastError(null);
        });
    }

    private void markAsFailedAttempt(UUID eventId, String errorMessage) {
        transactionTemplate.executeWithoutResult(status -> {
            OutboxEvent event = outboxEventRepository.findById(eventId)
                    .orElseThrow();

            event.setAttempts(event.getAttempts() + 1);
            event.setLastError(errorMessage);

            if (event.getAttempts() >= properties.maxAttempts()) {
                event.setStatus(OutboxEventStatus.FAILED);
            } else {
                event.setStatus(OutboxEventStatus.NEW);
            }
        });
    }
}
