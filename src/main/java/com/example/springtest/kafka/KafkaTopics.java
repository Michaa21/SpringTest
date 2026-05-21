package com.example.springtest.kafka;

public final class KafkaTopics {

    public static final String STUDENT_CREATE_REQUESTS = "student-create-requests";
    public static final String EXTERNAL_STUDENT_CREATED_EVENTS = "external-student-created-events";
    public static final String DLQ_EVENTS = "student-events-dlq";

    private KafkaTopics() {
    }
}
