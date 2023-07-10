CREATE TABLE public.orders (
   id BIGINT PRIMARY KEY,
   address VARCHAR(255),
   status INT,
   created TIMESTAMP,
   updated TIMESTAMP
);

CREATE TABLE public.client (
    id BIGINT PRIMARY KEY,
    phone VARCHAR(15)
);