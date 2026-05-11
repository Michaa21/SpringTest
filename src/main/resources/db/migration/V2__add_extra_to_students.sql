ALTER TABLE spring_test.students
    ADD COLUMN extra TEXT,
    ADD COLUMN email VARCHAR(255),
    ADD COLUMN age INTEGER;

UPDATE spring_test.students
SET email = 'unknown@mail.com',
    age = 18
WHERE email IS NULL OR age IS NULL;

ALTER TABLE spring_test.students
    ALTER COLUMN email SET NOT NULL,
    ALTER COLUMN age SET NOT NULL;

CREATE TABLE external_student_compensation_task
(
    id UUID PRIMARY KEY,
    student_id UUID NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    attempts INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    completed_at TIMESTAMP WITH TIME ZONE
);