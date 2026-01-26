CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE spring_test.users
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(100) NOT NULL UNIQUE
);
