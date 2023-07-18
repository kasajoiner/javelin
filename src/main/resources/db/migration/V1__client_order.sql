CREATE TABLE public.orders (
   id BIGINT PRIMARY KEY,
   client_id BIGINT NOT NULL,
   price BIGINT NOT NULL,
   address VARCHAR(255),
   status INT,
   created TIMESTAMP,
   updated TIMESTAMP
);

CREATE TABLE public.client (
    id BIGINT PRIMARY KEY,
    phone VARCHAR(15)
);