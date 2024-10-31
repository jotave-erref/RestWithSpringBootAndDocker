# RestWithSpringBoot

**Uma API RESTful robusta para Cadastro e Manipulação de dados de Pessoas e Livros**


## Descrição

Este projeto fornece uma API RESTful para cadastro e manipulação de dados de pessoas e livros adicionadas em uma base de dados. A API utiliza Spring Boot para um desenvolvimento rápido e eficiente, Spring Data JPA para acesso a dados, Spring Security com JWT para autenticação segura e Flyway para gerenciamento de migrações de banco de dados. A documentação da API é gerada automaticamente com SpringDoc OpenAPI e a navegação é facilitada pelo uso de HATEOAS.

O projeto foi desenvolvido a partir do curso da escola de programação Udemy com aulas ministradas pelo professor Leandro Costa. O curso ensina os fundamentos centrais de Webservices API's REST
e RESTful tanto na teoria, quanto na prática. Esse conhecimento está sendo aplicado na implementação de uma API RESTful 
com SpringBoot 3 e Java 18.


## Funcionalidades
- Autenticação JWT.
- Operações CRUD com Spring Data JPA.
- Documentação automática com SpringDoc OpenAPI.
- Migrações com Flyway.
- Testes de integração com Testcontainers e Rest Assured.
- Navegação HATEOAS. Permite servir diferentes representações de um mesmo recurso (URI). Web Services que suportam vários formatos de saída (XML, JSON, etc.).
- Conceitos arquiteturais do REST/RESTful

## Tecnologias Utilizadas
- 
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.1-green.svg)](https://spring.io)
- [![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-data%20access-blue.svg)](https://spring.io/projects/spring-data-jpa)
- [![Spring Security](https://img.shields.io/badge/Spring%20Security-authentication-blue.svg)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JWT-authentication-blueviolet.svg)](https://jwt.io/)
- [![MySQL](https://img.shields.io/badge/MySQL-database-brightgreen.svg)](https://www.mysql.com/)
- [![Flyway](https://img.shields.io/badge/Flyway-database%20migrations-blue.svg)](https://flywaydb.org/)
- [![Testcontainers](https://img.shields.io/badge/Testcontainers-testing-blue.svg)](https://testcontainers.org/)
[![Rest Assured](https://img.shields.io/badge/Rest%20Assured-testing-blue.svg)](https://rest-assured.io/)
- [![SpringDoc OpenAPI](https://img.shields.io/badge/SpringDoc%20OpenAPI-documentation-blue.svg)](https://springdoc.org/)
- ![Postman](https://img.shields.io/badge/Postman-API%20testing-blue.svg)
- ![HATEOAS](https://img.shields.io/badge/HATEOAS-hypermedia-blue.svg)
- ![Content Negotiation](https://img.shields.io/badge/Content%20Negotiation-support-blue.svg)
- ![Swagger UI](https://img.shields.io/badge/Swagger%20UI-documentation-blue.svg)

## Pré-requisitos
- Java 17
- Maven
- MySQL

## Como Rodar o Projeto
1. Configure o banco de dados no `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/seubancodedados
   spring.datasource.username=seuusuario
   spring.datasource.password=suasenha

2. Execute
   `bash:`
   ```bash
   mvn clean install
   mvn spring-boot:run


Documentação da API
Acesse em: http://localhost:8080/swagger-ui/index.html#/

Exemplos de Uso
Exemplo de chamada para autenticação:

**HTTP**
```HTTP
POST /api/auth/login
````

**JSON**
```JSON
{
  "username": "usuario",
  "password": "senha"
}
```
**XML**
```XML
   <body>
      <username>usuario</username>
      <password>senha</password>
   </body>

```

## Funcionalidades

* **Autenticação:** Os usuários se autenticam através de suas credenciais, recebendo um token JWT que deve ser incluído no cabeçalho das requisições subsequentes.
* **CRUD:** É possível criar, ler, atualizar e deletar [recursos].
* **Documentação:** A documentação interativa da API está disponível em http://localhost:8080/swagger-ui/index.html#/.
* ...

## ...
