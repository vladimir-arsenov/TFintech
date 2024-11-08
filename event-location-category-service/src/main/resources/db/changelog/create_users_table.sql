CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    role     VARCHAR(255),
    CONSTRAINT uc_users_username UNIQUE (username)
);