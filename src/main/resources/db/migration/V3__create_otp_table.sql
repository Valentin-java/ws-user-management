CREATE TABLE IF NOT EXISTS "ws01_otp" (
    id BIGSERIAL NOT NULL,
    uuid VARCHAR(255) NOT NULL,
    otp VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    type_otp VARCHAR(255),
    created_at TIMESTAMP,
    status_otp VARCHAR(255)
    );