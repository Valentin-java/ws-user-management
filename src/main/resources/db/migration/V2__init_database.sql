CREATE TABLE IF NOT EXISTS "ws01_user_profile" (
    id BIGSERIAL NOT NULL,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    activity_status VARCHAR(255) NOT NULL,
    customer_role VARCHAR(255),
    text_error VARCHAR(255),
    otp BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    last_visit_at TIMESTAMP,
    CONSTRAINT ws01_user_profile_pkey PRIMARY KEY (id),
    CONSTRAINT uk_username UNIQUE (username)
    );

COMMENT ON COLUMN "ws01_user_profile".activity_status IS 'Possible values: ACTIVE, INACTIVE';