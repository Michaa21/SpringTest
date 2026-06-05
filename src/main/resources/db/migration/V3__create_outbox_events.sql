create schema if not exists spring_test;

create table if not exists spring_test.outbox_events
(
    id             uuid primary key,
    event_id       uuid                     not null unique,
    aggregate_type varchar(255)             not null,
    aggregate_id   uuid                     not null,
    event_type     varchar(255)             not null,
    topic          varchar(255)             not null,
    payload        text                     not null,
    status         varchar(50)              not null,
    attempts       integer                  not null default 0,
    last_error     text,
    created_at     timestamp with time zone not null,
    processing_started_at timestamp with time zone,
    published_at   timestamp with time zone
);

create index if not exists idx_outbox_events_status_created_at
    on spring_test.outbox_events (status, created_at);
