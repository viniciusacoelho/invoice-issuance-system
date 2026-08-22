![Java](https://img.shields.io/badge/Java-26-orange?logo=openjdk)
![Spring](https://img.shields.io/badge/Spring-4-green?logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-blue?logo=postgresql)
![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)

# Sistema de Emissão de Notas Fiscais

API REST para gerenciamento de produtos e emissão de notas fiscais, desenvolvida com **Java** e **Spring Boot**, utilizando **PostgreSQL** para persistência dos dados.

O projeto tem como objetivo aplicar conceitos de desenvolvimento de APIs REST, persistência de dados, regras de negócio, tratamento de exceções e gerenciamento de estoque em um sistema de emissão de notas fiscais.

## Tecnologias

* **Java**
* **Spring Boot**
* **Spring Data JPA**
* **Hibernate**
* **PostgreSQL**
* **Maven**
* **Bean Validation**
* **JUnit**
* **Mockito**
* **Swagger / OpenAPI**
* **Git**

## Funcionalidades

### Produtos

* Cadastro de produtos
* Consulta de produtos
* Atualização de produtos
* Controle de estoque
* Validação dos dados informados

Cada produto possui:

* Código
* Nome/descrição
* Preço
* Quantidade disponível em estoque
* Categoria

### Notas Fiscais

* Criação de notas fiscais
* Numeração sequencial
* Status da nota fiscal
* Adição de múltiplos produtos
* Definição da quantidade de cada produto
* Consulta de notas fiscais
* Atualização do estoque de acordo com os produtos utilizados
* Fechamento da nota fiscal durante a operação de emissão

As notas fiscais possuem os seguintes status:

* `OPEN` — Aberta
* `CLOSED` — Fechada

Uma nota fiscal só pode ser emitida enquanto estiver aberta. Após a emissão, seu status é atualizado para fechada e as quantidades utilizadas são descontadas do estoque dos produtos.

## Regras de negócio

O sistema possui algumas regras para garantir a consistência das operações:

* A numeração das notas fiscais é sequencial.
* Uma nota fiscal é criada inicialmente com status `OPEN`.
* Apenas notas fiscais abertas podem ser emitidas.
* Um produto precisa estar previamente cadastrado para ser utilizado em uma nota.
* A quantidade adicionada à nota fiscal deve ser compatível com o estoque disponível.
* Ao emitir uma nota fiscal, o estoque dos produtos utilizados é atualizado.
* Uma nota fiscal fechada não pode ser emitida novamente.
* Operações inválidas retornam respostas de erro apropriadas pela API.

## Arquitetura

O projeto foi desenvolvido como uma **aplicação backend monolítica**, utilizando uma arquitetura baseada nas responsabilidades da aplicação.

```text
Client
  │
  ▼
Controller
  │
  ▼
Service
  │
  ▼
Repository
  │
  ▼
PostgreSQL
```

A aplicação é organizada em camadas para separar responsabilidades:

* **Controller** — recebe e processa as requisições HTTP.
* **Service** — concentra as regras de negócio.
* **Repository** — realiza o acesso aos dados.
* **Entity** — representa as entidades persistidas no banco.
* **DTO** — define os objetos utilizados na comunicação da API.
* **Exception** — centraliza o tratamento de erros e exceções.

## Banco de dados

O projeto utiliza **PostgreSQL** como banco de dados relacional.

A persistência é realizada utilizando **Spring Data JPA** e **Hibernate**, permitindo o mapeamento entre as entidades Java e as tabelas do banco de dados.

Principais entidades:

```text
Product
   │
   │
   └────────── InvoiceItem
                    │
                    │
                    ▼
                  Invoice
```

Uma nota fiscal pode possuir vários itens, e cada item referencia um produto e sua respectiva quantidade.

## API REST

A aplicação disponibiliza endpoints HTTP para gerenciamento dos recursos.

### Produtos

| Método   | Endpoint         | Descrição           |
| -------- | ---------------- | ------------------- |
| `POST`   | `/products`      | Cadastra um produto |
| `GET`    | `/products`      | Lista os produtos   |
| `GET`    | `/products/{id}` | Busca um produto    |
| `PUT`    | `/products/{id}` | Atualiza um produto |
| `DELETE` | `/products/{id}` | Remove um produto   |

### Notas Fiscais

| Método | Endpoint               | Descrição                    |
| ------ | ---------------------- | ---------------------------- |
| `POST` | `/invoices`            | Cria uma nota fiscal         |
| `GET`  | `/invoices`            | Lista as notas fiscais       |
| `GET`  | `/invoices/{id}`       | Busca uma nota fiscal        |
| `PUT`  | `/invoices/{id}`       | Atualiza os produtos da nota |
| `POST` | `/invoices/{id}/issue` | Emite uma nota fiscal        |

> Os endpoints podem evoluir conforme novas funcionalidades forem adicionadas ao projeto.

## Tratamento de exceções

A API possui tratamento de exceções para situações como:

* Produto não encontrado;
* Nota fiscal não encontrada;
* Produto sem estoque suficiente;
* Tentativa de emitir uma nota já fechada;
* Dados de entrada inválidos;
* Operações incompatíveis com as regras de negócio.

As exceções são convertidas em respostas HTTP apropriadas, permitindo que o cliente da API identifique o motivo da falha.

## Documentação da API

A API pode ser explorada e testada utilizando **Swagger/OpenAPI**.

Após iniciar a aplicação, a documentação estará disponível através da interface do Swagger configurada no projeto.

## Como executar

### Pré-requisitos

Antes de executar o projeto, é necessário ter instalado:

* Java
* Maven
* PostgreSQL

### Configuração do banco

Crie um banco de dados PostgreSQL para a aplicação e configure as propriedades de conexão no arquivo de configuração do Spring Boot.

Exemplo:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/invoice_issuance_db
    username: seu_usuario
    password: sua_senha

  jpa:
    hibernate:
      ddl-auto: update
```

### Executando a aplicação

Clone o repositório:

```bash
git clone https://github.com/viniciusacoelho/invoice-issuance-system.git
```

Acesse o diretório:

```bash
cd invoice-issuance-system
```

Execute a aplicação utilizando Maven:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

## Testes

O projeto utiliza **JUnit** e **Mockito** para testes automatizados das funcionalidades e regras de negócio da aplicação.

Para executar os testes:

```bash
./mvnw test
```

No Windows:

```bash
mvnw.cmd test
```

[//]: # ()
[//]: # (## Próximos passos)

[//]: # ()
[//]: # (Algumas funcionalidades que podem ser incorporadas ao projeto futuramente:)

[//]: # ()
[//]: # (* [ ] Implementar testes de integração)

[//]: # (* [ ] Aprimorar a documentação da API)

[//]: # (* [ ] Implementar controle de concorrência no estoque)

[//]: # (* [ ] Implementar idempotência nas operações críticas)

[//]: # (* [ ] Adicionar autenticação e autorização)

[//]: # (* [ ] Containerizar a aplicação com Docker)

[//]: # (* [ ] Criar pipeline de CI/CD)

[//]: # (* [ ] Adicionar observabilidade e monitoramento)

## Objetivo do projeto

Este projeto é utilizado como forma de estudo e prática de desenvolvimento backend com **Java e Spring Boot**, explorando a construção de uma API REST completa, integração com banco de dados relacional, organização em camadas, aplicação de regras de negócio, tratamento de exceções e testes automatizados.

---

Desenvolvido por **Vinícius Araújo Coêlho**.
