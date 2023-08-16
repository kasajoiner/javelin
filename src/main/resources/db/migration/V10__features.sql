CREATE TABLE if not exists public.features (
    id              BIGSERIAL   PRIMARY KEY,
    order_counter    BOOLEAN     NOT NULL default false
);