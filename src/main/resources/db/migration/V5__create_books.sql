CREATE TABLE spring_test.books
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    author_id BIGINT NOT NULL,

    CONSTRAINT fk_book_author
        FOREIGN KEY (author_id)
            REFERENCES spring_test.authors (id)
            ON DELETE CASCADE
);
