CREATE TABLE category (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(120) NOT NULL UNIQUE,
    description VARCHAR(500)
);

CREATE TABLE product (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(150) NOT NULL,
    description     VARCHAR(1000),
    price           NUMERIC(12, 2) NOT NULL CHECK (price >= 0),
    stock_quantity  INTEGER NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    category_id     BIGINT NOT NULL REFERENCES category (id),
    created_at      TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_product_category_id ON product (category_id);
