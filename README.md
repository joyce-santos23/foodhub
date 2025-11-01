# 🍔 FoodHub Backend API

O FoodHub é uma API de gerenciamento de usuários e endereços para estabelecimentos desenvolvida em Spring Boot 3.3.5 (Java 21), utilizando PostgreSQL e Docker Compose. O projeto implementa autenticação JWT (JSON Web Token) e controle de acesso baseado em Papéis (RBAC) para garantir a segurança dos recursos.

## 🔑 Arquitetura e Segurança

* Framework: Spring Boot 3.3.5

* Banco de Dados: PostgreSQL 15

* Orquestração: Docker Compose

* Autenticação: JWT (JSON Web Token)

* Autorização: Spring Security com @PreAuthorize e Papéis (ADMIN, OWNER, CUSTOMER).

* Documentação: Springdoc OpenAPI (Swagger UI).

## 🚀 Como Iniciar o Projeto

Este projeto utiliza o Docker Compose para criar e orquestrar a aplicação Spring Boot e o banco de dados PostgreSQL.

### Pré-requisitos

1. Docker e Docker Compose instalados.

2. JDK 21 instalado localmente.

### 1. Build e Execução

- Configure o arquive .env com as credenciais para construir o banco e gerar a chave de token usando o formato Base64 (olhar arquivo .env de exemplo).
- Utilize o Docker Compose para construir a imagem e iniciar os containers de forma limpa:

### Para garantir que todas as dependências e o código corrigido sejam compilados
```docker-compose up -d --build```

**Verificação de Logs:**
Monitore os logs para confirmar que a aplicação Spring Boot iniciou sem erros:

```docker-compose logs -f foodhub_app```

A aplicação estará pronta quando a mensagem Tomcat started on port 8080 aparecer.

## 🔗 Acessando a Documentação e Endpoints

A documentação interativa (Swagger UI) está acessível diretamente no navegador.

| Recurso | URL | Função 
| ----- | ----- | ----- 
| **Swagger UI** | http://localhost:8080/swagger-ui/index.html | Interface para testar todos os endpoints. 

### Endpoints da API (/api/v1)

## Auth
| Método | Endpoint                    | Descrição                             | Permissão   |
| ------ | --------------------------- | ------------------------------------- | ----------- |
| POST   | `/auth/login`               | Autenticação e geração de token JWT   | Público     |
| PUT    | `/auth/change-password`     | Alteração de senha do próprio usuário | Autenticado |
| PUT    | `/auth/{id}/password-reset` | Reset de senha de outro usuário       | ADMIN       |

## Customers
| Método | Endpoint           | Descrição                            | Permissão   |
| ------ | -----------------  | ------------------------------------ | ----------- |
| GET    | `/customers`       | Listar todos os clientes (paginação) | ADMIN       |
| GET    | `/customers/{id}`  | Consultar cliente por ID             | ADMIN       |
| GET    | `/customers/search`| Consultar cliente pelo nome          | ADMIN       |
| GET    | `/customers/me`    | Consultar perfil do usuário logado   | Autenticado |
| POST   | `/customers`       | Criar novo cliente                   | Público     |
| PUT    | `/customers/{id}`  | Atualizar cliente                    | Autenticado |
| DELETE | `/customers/{id}`  | Deletar cliente                      | Autenticado |

## Owners
| Método | Endpoint           | Descrição                                 | Permissão   |
| ------ | -------------------| ----------------------------------------- | ----------- |
| GET    | `/owners`          | Listar todos os proprietários (paginação) | ADMIN       |
| GET    | `/owners/{id}`     | Consultar proprietário por ID             | ADMIN       |
| GET    | `/customers/search`| Consultar proprietário pelo nome          | ADMIN       |
| GET    | `/owners/me`       | Consultar perfil do proprietário logado   | Autenticado |
| POST   | `/owners`          | Criar novo proprietário                   | Público     |
| PUT    | `/owners/{id}`     | Atualizar proprietário                    | Autenticado |
| DELETE | `/owners/{id}`     | Deletar proprietário                      | Autenticado |

## User Address
| Método | Endpoint                        | Descrição                     | Permissão   |
| ------ | ------------------------------- | ----------------------------- | ----------- |
| GET    | `/{userId}/address`             | Listar endereços do usuário   | Autenticado |
| POST   | `/{userId}/address`             | Criar endereço para usuário   | Autenticado |
| PUT    | `/{userId}/address/{addressId}` | Atualizar endereço do usuário | Autenticado |
| DELETE | `/{userId}/address/{addressId}` | Deletar endereço do usuário   | Autenticado |

### Como Testar Rotas Protegidas (JWT)

1. Obter Token: No Swagger UI, use o endpoint POST /api/v1/auth/login com as credenciais cadastradas.

2. Autorizar: Copie o accessToken retornado.

3. Colar: Clique no botão "Authorize" no topo do Swagger UI e cole o token no campo, usando o formato **Bearer <Token>**.

4. Testar: Tente acessar uma rota restrita.

## 🛑 Comandos Úteis

| Comando | Descrição 
| ----- | ----- 
| docker-compose down | Para e remove os containers e a rede (ideal antes do build). 
| docker-compose down --volumes | Para e remove containers E o volume de dados do PostgreSQL (limpeza total do banco). 
| docker-compose logs -f foodhub_app | Exibe os logs da aplicação em tempo real.
