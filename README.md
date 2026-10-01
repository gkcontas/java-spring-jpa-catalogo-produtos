# Catálogo de Produtos

API REST para gerenciar um catálogo de produtos organizado por categorias, com controle de estoque.

O projeto mostra como montar uma aplicação Spring Boot sobre um banco relacional de ponta a ponta: mapeamento JPA das entidades, validação dos dados de entrada, evolução do schema por migrations versionadas e testes de integração que rodam contra um PostgreSQL real.

## Tecnologias e bibliotecas

| | |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.3 |
| Persistência | Spring Data JPA / Hibernate, PostgreSQL 16 |
| Migrations | Flyway |
| Validação | Bean Validation (Hibernate Validator) |
| Build | Maven (wrapper `mvnw`) |
| Testes | JUnit 5, Mockito, Testcontainers |
| Apoio | Lombok nas entidades |

## Pré-requisitos

- JDK 17 ou superior
- Docker (para o banco e para os testes de integração)

## Como rodar

```bash
docker compose up -d
```

```bash
./mvnw spring-boot:run
```

A API fica disponível em `http://localhost:8080`. O Flyway cria o schema automaticamente na primeira subida.

Para parar o banco ao terminar:

```bash
docker compose down
```

## Endpoints

### Categorias

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/categories` | Lista todas as categorias |
| `GET` | `/categories/{id}` | Busca uma categoria |
| `POST` | `/categories` | Cria uma categoria |
| `PUT` | `/categories/{id}` | Atualiza uma categoria |
| `DELETE` | `/categories/{id}` | Remove uma categoria |

### Produtos

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/products?categoryId=&page=&size=` | Lista paginada, com filtro opcional por categoria |
| `GET` | `/products/{id}` | Busca um produto |
| `POST` | `/products` | Cria um produto |
| `PUT` | `/products/{id}` | Atualiza um produto |
| `DELETE` | `/products/{id}` | Remove um produto |
| `POST` | `/products/{id}/stock/increase` | Dá entrada em estoque |
| `POST` | `/products/{id}/stock/decrease` | Dá baixa em estoque (422 se não houver saldo) |

## Exemplos de uso

```bash
curl -s -X POST localhost:8080/categories \
  -H "Content-Type: application/json" \
  -d '{"name": "Computing", "description": "Computing products"}'
```

```bash
curl -s -X POST localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Mouse", "description": "Wireless mouse", "price": 99.90, "initialStockQuantity": 10, "categoryId": 1}'
```

```bash
curl -s "localhost:8080/products?categoryId=1&page=0&size=10"
```

```bash
curl -s -X POST localhost:8080/products/1/stock/increase \
  -H "Content-Type: application/json" \
  -d '{"quantity": 5}'
```

```bash
curl -s -X POST localhost:8080/products/1/stock/decrease \
  -H "Content-Type: application/json" \
  -d '{"quantity": 3}'
```

## Testes

```bash
./mvnw test
```

13 testes: 8 unitários, que rodam sem dependência externa, e 5 de integração, que sobem um PostgreSQL em container pelo Testcontainers. Docker precisa estar disponível para a suíte completa.

## Estrutura

```
src/main/java/com/gkcontas/catalog
├── category      entidade, repositório, serviço e controller de categorias
├── product       entidade, repositório, serviço e controller de produtos
└── common        tratamento de erros e DTOs compartilhados

src/main/resources/db/migration    migrations Flyway
```
