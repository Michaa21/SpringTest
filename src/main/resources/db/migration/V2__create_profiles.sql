CREATE TABLE spring_test.profiles
(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name  VARCHAR(100),
    user_id    UUID NOT NULL UNIQUE,

    CONSTRAINT fk_profile_user
        FOREIGN KEY (user_id)
            REFERENCES spring_test.users (id)
            ON DELETE CASCADE
);
