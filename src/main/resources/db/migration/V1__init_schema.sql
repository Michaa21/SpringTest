CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE spring_test.users
(
    id       UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE spring_test.profiles
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name  VARCHAR(100),
    user_id    UUID NOT NULL UNIQUE,

    CONSTRAINT fk_profile_user
        FOREIGN KEY (user_id)
            REFERENCES spring_test.users (id)
            ON DELETE CASCADE
);

CREATE TABLE spring_test.authors
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE spring_test.books
(
    id        BIGSERIAL PRIMARY KEY,
    title     VARCHAR(100) NOT NULL,
    author_id BIGINT       NOT NULL,

    CONSTRAINT fk_book_author
        FOREIGN KEY (author_id)
            REFERENCES spring_test.authors (id)
            ON DELETE CASCADE
);

CREATE TABLE spring_test.students
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE spring_test.lessons
(
    id    BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL
);

CREATE TABLE spring_test.student_lesson
(
    student_id BIGINT NOT NULL,
    lesson_id  BIGINT NOT NULL,

    CONSTRAINT pk_student_lesson
        PRIMARY KEY (student_id, lesson_id),

    CONSTRAINT fk_student_lesson_student
        FOREIGN KEY (student_id)
            REFERENCES spring_test.students (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_student_lesson_lesson
        FOREIGN KEY (lesson_id)
            REFERENCES spring_test.lessons (id)
            ON DELETE CASCADE
);
