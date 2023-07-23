CREATE TABLE public.employee (
   id BIGINT PRIMARY KEY,
   role VARCHAR(15) NOT NULL,
   status VARCHAR(15) NOT NULL default 'ENABLED'
);