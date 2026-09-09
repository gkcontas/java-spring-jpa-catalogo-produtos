# Planejamento — Catálogo de Produtos

## Objetivo

Demonstrar o uso "básico e correto" de Spring Data JPA/Hibernate sobre PostgreSQL em uma API REST CRUD, com migrations de banco versionadas (Flyway) e testes de integração rodando contra um Postgres real via Testcontainers — em vez de H2 em memória.

## Escopo do MVP

Incluído:
- CRUD de `Categoria` e `Produto` (produto pertence a uma categoria)
- Controle simples de estoque (quantidade em `Produto`, endpoint de entrada/saída de estoque)
- Validação de payloads (Bean Validation)
- Paginação e ordenação na listagem de produtos
- Tratamento de erros padronizado (`@ControllerAdvice`)

Fora de escopo:
- Autenticação/autorização (fica para o projeto `java-spring-jwt-api-auth`)
- Múltiplos armazéns/localizações de estoque
- Frontend

## Stack e justificativa

- **Spring Boot** — framework mais comum no mercado Java, bom para mostrar convenções REST idiomáticas
- **Spring Data JPA + Hibernate** — ORM mais usado em projetos Java corporativos
- **PostgreSQL** — banco relacional open-source mais comum em vagas
- **Flyway** — versionamento de schema, prática esperada em produção
- **Testcontainers** — testes de integração contra Postgres real (evita falso positivo de H2)

## Arquitetura

Aplicação monolítica simples em camadas:

```
Controller (REST) → Service → Repository (Spring Data JPA) → PostgreSQL
```

Migrations do Flyway aplicadas automaticamente no startup (`classpath:db/migration`).

## Modelo de dados

- `categoria (id, nome, descricao)`
- `produto (id, nome, descricao, preco, quantidade_estoque, categoria_id FK)`

## Endpoints principais

- `POST /categorias`, `GET /categorias`, `GET /categorias/{id}`, `PUT /categorias/{id}`, `DELETE /categorias/{id}`
- `POST /produtos`, `GET /produtos` (paginado, filtro por categoria), `GET /produtos/{id}`, `PUT /produtos/{id}`, `DELETE /produtos/{id}`
- `POST /produtos/{id}/estoque/entrada`, `POST /produtos/{id}/estoque/saida`

## Estratégia de testes

- Testes unitários de `Service` (regras de estoque, validações) com Mockito
- Testes de integração de `Repository`/`Controller` com Testcontainers (Postgres real), usando `@SpringBootTest` + `@Testcontainers`
- Testes de migration (Flyway aplica limpo em banco novo)

## Roadmap de implementação

1. Setup do projeto (Spring Initializr: Web, JPA, Validation, Flyway, PostgreSQL driver, Testcontainers)
2. Modelo de dados + primeira migration Flyway
3. Endpoints CRUD de `Categoria`
4. Endpoints CRUD de `Produto` + regras de estoque
5. Testes de integração com Testcontainers
6. `docker-compose.yml` com Postgres para rodar localmente
7. README final com instruções de execução e exemplos de request
8. (Opcional) GitHub Actions: build + testes no push

## Critérios de "pronto" para portfólio

- README com instruções claras de setup (`docker-compose up`, `mvn spring-boot:run`)
- Testes automatizados passando (`mvn test`)
- Exemplos de request (curl ou collection) documentados
- Código organizado em pacotes por camada (`controller`, `service`, `repository`, `model`, `dto`)
