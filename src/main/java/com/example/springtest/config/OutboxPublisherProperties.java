package com.example.springtest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "outbox.publisher")
public record OutboxPublisherProperties(int maxAttempts) {
}
