CREATE TABLE categoria (
    id          BIGSERIAL PRIMARY KEY,
    nome        VARCHAR(120) NOT NULL UNIQUE,
    descricao   VARCHAR(500)
);

CREATE TABLE produto (
    id                  BIGSERIAL PRIMARY KEY,
    nome                VARCHAR(150) NOT NULL,
    descricao           VARCHAR(1000),
    preco               NUMERIC(12, 2) NOT NULL CHECK (preco >= 0),
    quantidade_estoque  INTEGER NOT NULL DEFAULT 0 CHECK (quantidade_estoque >= 0),
    categoria_id        BIGINT NOT NULL REFERENCES categoria (id),
    criado_em           TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_produto_categoria_id ON produto (categoria_id);
