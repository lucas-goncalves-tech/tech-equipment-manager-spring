CREATE TABLE users
(
    id                 UUID                        NOT NULL,
    email              VARCHAR(200)                NOT NULL,
    display_name       VARCHAR(255)                NOT NULL,
    password_hash      VARCHAR(100)                NOT NULL,
    role               VARCHAR(50)                 NOT NULL CHECK (role IN ('ROLE_USER', 'ROLE_ADMIN')),
    is_active          BOOLEAN                     NOT NULL DEFAULT TRUE,
    deactivated_reason VARCHAR(255),
    created_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uc_users_email UNIQUE (email)
);

CREATE INDEX idx_users_created_at ON users(created_at DESC);
