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

    @Scheduled(fixedDelayString = "${outbox.publisher.fixed-delay-ms}")
    public void publishNewEvents() {
        List<OutboxEvent> events = fetchAndMarkProcessing();

        for (OutboxEvent event : events) {
            publishOne(event);
        }
    }

    private List<OutboxEvent> fetchAndMarkProcessing() {
        return transactionTemplate.execute(status -> {
            OffsetDateTime now = OffsetDateTime.now();
            OffsetDateTime timeout = now.minusSeconds(properties.processingTimeoutSeconds());

            int resetCount = outboxEventRepository.resetStuckProcessingEvents(timeout);

            if (resetCount > 0) {
                log.warn("Reset {} stuck outbox events from PROCESSING to NEW", resetCount);
            }

            List<OutboxEvent> events = outboxEventRepository
                    .findTop10ByStatusForUpdateSkipLocked(OutboxEventStatus.NEW.name());

            for (OutboxEvent event : events) {
                event.setStatus(OutboxEventStatus.PROCESSING);
                event.setProcessingStartedAt(now);
            }
            return events;
        });
    }

    private void publishOne(OutboxEvent event) {

        kafkaTemplate.send(
                event.getTopic(),
                event.getAggregateId().toString(),
                event.getPayload()
        ).whenComplete((result, exception) -> {
            if (exception != null) {
                markAsFailedAttempt(event.getId(), exception.getMessage());
                log.error("Failed to publish outbox event {}", event.getEventId(), exception);
                return;
            }

            try {
                markAsPublished(event.getId());

                log.info("Outbox event {} published to topic {}",
                        event.getEventId(), event.getTopic());
            } catch (Exception statusUpdateException) {
                log.error("Outbox event {} was sent to Kafka, but status update failed",
                        event.getEventId(), statusUpdateException);
            }
        });
    }

    private void markAsPublished(UUID eventId) {
        transactionTemplate.executeWithoutResult(status -> {
            int updated = outboxEventRepository.markAsPublishedIfProcessing(
                    eventId,
                    OffsetDateTime.now()
            );
            if (updated == 0) {
                log.warn("Outbox event {} was not marked as PUBLISHED because it is not PROCESSING", eventId);
            }
        });
    }

    private void markAsFailedAttempt(UUID eventId, String errorMessage) {
        transactionTemplate.executeWithoutResult(status -> {
            int updated = outboxEventRepository.markAsFailedAttemptIfProcessing(
                    eventId,
                    errorMessage,
                    properties.maxAttempts()
            );

            if (updated == 0) {
                log.warn("Outbox event {} was not marked as failed attempt because it is not PROCESSING", eventId);
            }
        });
    }
}
