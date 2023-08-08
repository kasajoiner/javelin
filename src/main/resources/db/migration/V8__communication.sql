CREATE TABLE if not exists public.communication (
    id BIGSERIAL PRIMARY KEY,
    txt TEXT NULL,
    "status" varchar(10) NOT NULL,
    "type" varchar(10) NOT NULL,
    object_id varchar(255) NULL,
    object_url varchar(255) NULL,
    sender BIGINT NOT NULL,
    receiver varchar(20) NOT NULL,
    created TIMESTAMP,
    updated TIMESTAMP
);