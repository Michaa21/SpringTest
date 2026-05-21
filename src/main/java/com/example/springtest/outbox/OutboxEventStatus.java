package com.example.springtest.outbox;

public enum OutboxEventStatus {
    NEW,
    PUBLISHED,
    FAILED
}