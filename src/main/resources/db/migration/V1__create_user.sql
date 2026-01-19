CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users
(
    id UUID PRIMARY KEY, --DEFAULT uuid_generate_v4(),
    username TEXT
);

CREATE TABLE IF NOT EXISTS profiles
(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name  VARCHAR(100),
    user_id UUID NOT NULL,
    CONSTRAINT fk_profile_user FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
);
