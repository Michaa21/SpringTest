CREATE SCHEMA IF NOT EXISTS spring_test;

CREATE TABLE IF NOT EXISTS spring_test.authors
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS spring_test.books
(
    id BIGSERIAL PRIMARY KEY,
    title     VARCHAR(100),
    author_id BIGINT NOT NULL,
    CONSTRAINT fk_book_author FOREIGN KEY (author_id)
        REFERENCES spring_test.authors (id)
        ON DELETE CASCADE
);
