# Catálogo de Produtos

API REST de catálogo de produtos (categorias, produtos, estoque) em Java com Spring Boot, Spring Data JPA/Hibernate e PostgreSQL, com migrations versionadas via Flyway e testes de integração com Testcontainers.

## Status

✅ MVP implementado — ver [PLANNING.md](PLANNING.md) para o planejamento original.

## Stack

- Java 17 + Spring Boot 3.3
- Spring Data JPA / Hibernate
- PostgreSQL
- Flyway (migrations)
- Bean Validation
- Testcontainers (testes de integração) + JUnit 5 + Mockito

## Como rodar

1. Suba o PostgreSQL:
   ```bash
   docker compose up -d
   ```
2. Rode a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
3. A API sobe em `http://localhost:8080`.

## Como rodar os testes

```bash
./mvnw test
```

Os testes de integração usam Testcontainers e sobem um PostgreSQL real em container — é necessário ter Docker disponível.

## Endpoints principais

### Categorias (`/categories`)

| Método | Rota             | Descrição              |
|--------|------------------|-------------------------|
| GET    | `/categories`    | Lista todas as categorias |
| GET    | `/categories/{id}`| Busca categoria por id |
| POST   | `/categories`    | Cria categoria          |
| PUT    | `/categories/{id}`| Atualiza categoria     |
| DELETE | `/categories/{id}`| Remove categoria       |

### Produtos (`/products`)

| Método | Rota                          | Descrição                              |
|--------|--------------------------------|-----------------------------------------|
| GET    | `/products?categoryId=&page=&size=` | Lista produtos paginados, com filtro opcional por categoria |
| GET    | `/products/{id}`              | Busca produto por id                    |
| POST   | `/products`                   | Cria produto                            |
| PUT    | `/products/{id}`              | Atualiza produto                        |
| DELETE | `/products/{id}`              | Remove produto                          |
| POST   | `/products/{id}/stock/increase`| Adiciona quantidade ao estoque         |
| POST   | `/products/{id}/stock/decrease`| Remove quantidade do estoque (422 se insuficiente) |

## Exemplo de uso

```bash
# Criar categoria
curl -s -X POST localhost:8080/categories \
  -H "Content-Type: application/json" \
  -d '{"name": "Computing", "description": "Computing products"}'

# Criar produto (categoryId retornado acima)
curl -s -X POST localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Mouse", "description": "Wireless mouse", "price": 99.90, "initialStockQuantity": 10, "categoryId": 1}'

# Dar entrada em estoque
curl -s -X POST localhost:8080/products/1/stock/increase \
  -H "Content-Type: application/json" \
  -d '{"quantity": 5}'
```
