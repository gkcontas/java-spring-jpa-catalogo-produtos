# Planejamento — Catálogo de Produtos

## Objetivo

Demonstrar o uso "básico e correto" de Spring Data JPA/Hibernate sobre PostgreSQL em uma API REST CRUD, com migrations de banco versionadas (Flyway) e testes de integração rodando contra um Postgres real via Testcontainers — em vez de H2 em memória.

## Escopo do MVP

Incluído:
- CRUD de `Category` e `Product` (produto pertence a uma categoria)
- Controle simples de estoque (quantidade em `Product`, endpoint de entrada/saída de estoque)
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

- `category (id, name, description)`
- `product (id, name, description, price, stock_quantity, category_id FK)`

## Endpoints principais

- `POST /categories`, `GET /categories`, `GET /categories/{id}`, `PUT /categories/{id}`, `DELETE /categories/{id}`
- `POST /products`, `GET /products` (paginado, filtro por categoria), `GET /products/{id}`, `PUT /products/{id}`, `DELETE /products/{id}`
- `POST /products/{id}/stock/increase`, `POST /products/{id}/stock/decrease`

## Estratégia de testes

- Testes unitários de `Service` (regras de estoque, validações) com Mockito
- Testes de integração de `Repository`/`Controller` com Testcontainers (Postgres real), usando `@SpringBootTest` + `@Testcontainers`
- Testes de migration (Flyway aplica limpo em banco novo)

## Roadmap de implementação

1. Setup do projeto (Spring Initializr: Web, JPA, Validation, Flyway, PostgreSQL driver, Testcontainers)
2. Modelo de dados + primeira migration Flyway
3. Endpoints CRUD de `Category`
4. Endpoints CRUD de `Product` + regras de estoque
5. Testes de integração com Testcontainers
6. `docker-compose.yml` com Postgres para rodar localmente
7. README final com instruções de execução e exemplos de request
8. (Opcional) GitHub Actions: build + testes no push

## Convenção de nomenclatura

Todo o código deve ser escrito em inglês: classes, arquivos, métodos, campos/variáveis, pacotes, rotas REST, payloads JSON de request/response, mensagens de validação/erro retornadas pela API, e nomes de tabelas/colunas nas migrations. Seguir as convenções idiomáticas Java (PascalCase para classes, camelCase para métodos/campos, snake_case para colunas SQL). A documentação do projeto (este `PLANNING.md` e o `README.md`) permanece em português.

Pacote base: `com.gkcontas` (ex.: `com.gkcontas.<domínio-do-projeto>`), refletido também no `groupId` do `pom.xml`.

## Estratégia de commits

Cada etapa do roadmap acima deve gerar um ou mais commits pequenos e independentes, feitos à medida que a etapa é concluída — nunca um único commit grande ao final. Ordem sugerida (ajustável conforme o que for implementado primeiro): setup do projeto → schema/migrations (quando aplicável) → modelo de domínio → repositórios/DTOs → camada de service → camada de API (controllers/endpoints/handlers) → testes unitários → testes de integração (e E2E quando aplicável) → infraestrutura local e documentação (docker-compose, README).

Mensagens de commit seguem Conventional Commits (`feat:`, `fix:`, `test:`, `docs:`, `chore:`), descrevendo a etapa concluída. Cada commit deve deixar o projeto compilando.

## Critérios de "pronto" para portfólio

- README com instruções claras de setup (`docker-compose up`, `mvn spring-boot:run`)
- Testes automatizados passando (`mvn test`)
- Exemplos de request (curl ou collection) documentados
- Código organizado em pacotes por camada (`controller`, `service`, `repository`, `model`, `dto`)
