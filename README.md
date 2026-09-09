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

### Categorias

| Método | Rota              | Descrição              |
|--------|-------------------|-------------------------|
| GET    | `/categorias`     | Lista todas as categorias |
| GET    | `/categorias/{id}`| Busca categoria por id  |
| POST   | `/categorias`     | Cria categoria          |
| PUT    | `/categorias/{id}`| Atualiza categoria      |
| DELETE | `/categorias/{id}`| Remove categoria        |

### Produtos

| Método | Rota                            | Descrição                              |
|--------|----------------------------------|-----------------------------------------|
| GET    | `/produtos?categoriaId=&page=&size=` | Lista produtos paginados, com filtro opcional por categoria |
| GET    | `/produtos/{id}`                | Busca produto por id                    |
| POST   | `/produtos`                     | Cria produto                            |
| PUT    | `/produtos/{id}`                | Atualiza produto                        |
| DELETE | `/produtos/{id}`                | Remove produto                          |
| POST   | `/produtos/{id}/estoque/entrada`| Adiciona quantidade ao estoque          |
| POST   | `/produtos/{id}/estoque/saida`  | Remove quantidade do estoque (422 se insuficiente) |

## Exemplo de uso

```bash
# Criar categoria
curl -s -X POST localhost:8080/categorias \
  -H "Content-Type: application/json" \
  -d '{"nome": "Informática", "descricao": "Produtos de informática"}'

# Criar produto (categoriaId retornado acima)
curl -s -X POST localhost:8080/produtos \
  -H "Content-Type: application/json" \
  -d '{"nome": "Mouse", "descricao": "Mouse sem fio", "preco": 99.90, "quantidadeEstoqueInicial": 10, "categoriaId": 1}'

# Dar entrada em estoque
curl -s -X POST localhost:8080/produtos/1/estoque/entrada \
  -H "Content-Type: application/json" \
  -d '{"quantidade": 5}'
```
