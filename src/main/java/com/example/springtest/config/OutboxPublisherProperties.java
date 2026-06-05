package com.example.springtest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "outbox.publisher")
public record OutboxPublisherProperties(int maxAttempts,
                                        long fixedDelayMs,
                                        long processingTimeoutSeconds) {
}
