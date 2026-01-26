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
