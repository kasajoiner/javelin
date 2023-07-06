CREATE TABLE category (
  id            BIGSERIAL PRIMARY KEY,
  title         VARCHAR(100) not null unique,
  external_id   VARCHAR(255) not null unique
);

CREATE TABLE product (
 id             BIGSERIAL PRIMARY KEY,
 title          VARCHAR(100) not null unique,
 external_id    VARCHAR(255) not null unique,
 created        TIMESTAMP,
 updated        TIMESTAMP
);

CREATE TABLE category_product (
  category_id   BIGINT,
  product_id    BIGINT,
  FOREIGN KEY (category_id) REFERENCES category(id),
  FOREIGN KEY (product_id) REFERENCES product(id),
  PRIMARY KEY (category_id, product_id)
);
