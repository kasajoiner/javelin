CREATE TABLE if not exists public.msg_template (
   id VARCHAR(200) PRIMARY KEY,
   txt TEXT NOT NULL,
   created TIMESTAMP,
   updated TIMESTAMP
);