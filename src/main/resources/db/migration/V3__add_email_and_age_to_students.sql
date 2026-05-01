ALTER TABLE spring_test.students
    ADD COLUMN email VARCHAR(255),
    ADD COLUMN age INTEGER;

UPDATE spring_test.students
SET email = 'unknown@mail.com',
    age = 18
WHERE email IS NULL OR age IS NULL;

ALTER TABLE spring_test.students
    ALTER COLUMN email SET NOT NULL,
    ALTER COLUMN age SET NOT NULL;