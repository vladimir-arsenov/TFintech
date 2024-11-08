CREATE TABLE tokens
(
    id         BIGSERIAL PRIMARY KEY,
    token      VARCHAR(255),
    token_type VARCHAR(255),
    expired    BOOLEAN NOT NULL,
    user_id    INTEGER,
    CONSTRAINT uc_tokens_token UNIQUE (token)
);
