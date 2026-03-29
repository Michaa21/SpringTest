CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE spring_test.users
(
    id       UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE spring_test.profiles
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
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
    id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL
);

CREATE TABLE spring_test.books
(
    id        UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title     VARCHAR(100) NOT NULL UNIQUE,
    author_id UUID         NOT NULL,

    CONSTRAINT fk_book_author
        FOREIGN KEY (author_id)
            REFERENCES spring_test.authors (id)
            ON DELETE CASCADE
);

CREATE TABLE spring_test.students
(
    id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL
);

CREATE TABLE spring_test.lessons
(
    id    UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(100) NOT NULL
);

CREATE UNIQUE INDEX uk_lessons_title_lower
    ON spring_test.lessons (LOWER(title));

CREATE TABLE spring_test.student_lesson
(
    student_id UUID NOT NULL,
    lesson_id  UUID NOT NULL,

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
