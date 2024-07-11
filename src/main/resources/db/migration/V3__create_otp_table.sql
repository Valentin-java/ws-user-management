CREATE TABLE IF NOT EXISTS "ws01_otp" (
    id BIGSERIAL NOT NULL,
    otp VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    activity_status VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP,
    last_visit_at TIMESTAMP
    );