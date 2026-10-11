![Java](https://img.shields.io/badge/Java-26-orange?logo=openjdk)
![Spring](https://img.shields.io/badge/Spring-4-green?logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-blue?logo=postgresql)
![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)

# Sistema de Emissão de Notas Fiscais

API REST para gerenciamento de produtos, clientes e emissão de notas fiscais, desenvolvida com **Java** e **Spring Boot**, utilizando **PostgreSQL** para persistência dos dados.

O projeto tem como objetivo aplicar conceitos de desenvolvimento backend, construção de APIs REST, persistência de dados, autenticação e autorização, integração com APIs externas, gerenciamento de estoque, concorrência, idempotência, cache, tratamento de exceções e testes automatizados.

## Tecnologias

- **Java**
- **Spring Boot**
- **Spring Web MVC**
- **Spring Data JPA**
- **Hibernate**
- **Spring Security**
- **JWT (JSON Web Token)**
- **Spring Cloud OpenFeign**
- **Spring Cache**
- **Bean Validation**
- **PostgreSQL**
- **Maven**
- **JUnit 5**
- **Mockito**
- **Swagger / OpenAPI**
- **Docker**
- **Lombok**
- **Git**

## Funcionalidades

### Produtos

- Cadastro de produtos
- Consulta de produtos
- Busca de produtos por nome
- Filtro de produtos por categoria
- Atualização de produtos
- Remoção de produtos
- Controle de estoque
- Validação dos dados informados
- Controle de concorrência em operações de estoque

Cada produto possui informações como:

- Código sequencial
- Nome
- Descrição
- Preço
- Quantidade disponível em estoque
- Categoria

### Notas Fiscais

- Criação de notas fiscais
- Numeração sequencial
- Status da nota fiscal
- Associação de cliente e usuário responsável
- Inclusão de múltiplos produtos
- Definição da quantidade de cada produto
- Adição de produtos em uma nota existente
- Remoção de produtos de uma nota
- Consulta de notas fiscais
- Consulta detalhada dos itens da nota
- Cálculo do preço total
- Cálculo da quantidade total de produtos
- Controle do estoque dos produtos
- Emissão de notas fiscais
- Registro da data e hora de emissão
- Proteção contra operações concorrentes no estoque
- Idempotência em operações críticas

As notas fiscais possuem os seguintes status:

- `OPEN` — Aberta
- `CLOSED` — Fechada

Uma nota fiscal somente pode ser emitida enquanto estiver aberta. Após a emissão, seu status é atualizado para fechada e a data e hora da emissão são registradas.

### Itens da Nota Fiscal

A entidade `InvoiceItem` representa os produtos pertencentes a uma nota fiscal.

Cada item mantém informações necessárias para identificar:

- A nota fiscal
- O produto
- A quantidade do produto utilizada na nota

Essa estrutura permite que uma mesma nota possua vários produtos com diferentes quantidades, evitando que a relação entre `Invoice` e `Product` seja responsável diretamente por armazenar a quantidade.

A estrutura pode ser representada de forma simplificada como:

```text
Invoice
   │
   │ 1
   │
   │ N
InvoiceItem
   │
   │ N
   │
   │ 1
Product
```

### Usuários e autenticação

O sistema possui gerenciamento de usuários utilizando **Spring Security** e autenticação baseada em **JWT (JSON Web Token)**.

Entre as funcionalidades estão:

- Cadastro de usuários
- Login
- Geração de token JWT
- Validação do token nas requisições
- Autenticação stateless
- Autorização baseada em papéis
- Proteção de endpoints de acordo com as permissões do usuário

Os usuários podem possuir papéis como:

- `ADMIN`
- `USER`

Após uma autenticação válida, a API retorna um token JWT que deve ser utilizado nas requisições protegidas.

```text
Authorization: Bearer <token>
```

O usuário autenticado representa o usuário do sistema responsável pelas operações realizadas na aplicação e é diferente do cliente associado à nota fiscal.

### Clientes

O sistema possui gerenciamento de clientes separado dos usuários responsáveis por utilizar a aplicação.

Entre as operações disponíveis estão:

- Cadastro de clientes
- Consulta de clientes
- Atualização de clientes
- Remoção de clientes
- Associação de clientes às notas fiscais
- Validação dos dados cadastrados
- Consulta automática de endereço pelo CEP

Cada nota fiscal pode ser associada ao cliente correspondente à operação.

### Endereços e ViaCEP

O sistema possui integração com a API do **ViaCEP** utilizando **Spring Cloud OpenFeign**.

Ao informar um CEP durante operações relacionadas ao cliente, a aplicação pode consultar automaticamente informações como:

- Logradouro
- Complemento
- Bairro
- Localidade
- UF
- Estado
- Região
- IBGE
- DDD

Os endereços consultados podem ser armazenados no banco de dados para evitar consultas externas desnecessárias.

A integração também utiliza **Spring Cache**, permitindo armazenar temporariamente consultas de CEP já realizadas e reduzir o tempo de resposta e a quantidade de chamadas para a API externa.

### Concorrência

O sistema possui controle de concorrência para evitar inconsistências durante alterações simultâneas no estoque.

Um exemplo de cenário tratado é:

```text
Estoque disponível: 1 unidade

Nota A ──┐
         ├── tenta utilizar o mesmo produto
Nota B ──┘
```

Sem controle de concorrência, as duas operações poderiam identificar uma unidade disponível e tentar utilizá-la simultaneamente.

O controle garante que apenas uma operação consiga consumir a última unidade disponível, evitando estoque negativo e inconsistências nos dados.

Dependendo da operação e da estratégia adotada, mecanismos de locking do JPA podem ser utilizados para controlar o acesso concorrente aos registros.

### Idempotência

Operações críticas da API podem utilizar **Idempotency Key** para impedir que uma mesma requisição seja processada mais de uma vez.

O cliente envia uma chave única através do header:

```text
Idempotency-Key: <chave-unica>
```

A aplicação registra a chave utilizada e associa o processamento ao recurso criado.

Caso a mesma requisição seja enviada novamente utilizando a mesma chave, o sistema consegue identificar que a operação já foi processada, evitando efeitos colaterais como:

- Criação duplicada de notas fiscais
- Processamento repetido da mesma operação
- Alterações duplicadas no estoque

A entidade `IdempotencyKey` é utilizada para armazenar as chaves processadas e relacioná-las às respectivas operações.

## Regras de negócio

O sistema possui regras para garantir a consistência das operações:

- A numeração das notas fiscais é sequencial.
- Uma nota fiscal é criada inicialmente com status `OPEN`.
- Apenas notas fiscais abertas podem ser emitidas.
- Uma nota fiscal fechada não pode ser emitida novamente.
- Um produto precisa estar previamente cadastrado para ser utilizado em uma nota.
- Cada item da nota representa um produto e sua respectiva quantidade.
- A quantidade solicitada deve ser compatível com o estoque disponível.
- O estoque de um produto nunca pode ficar negativo.
- Alterações no estoque são protegidas contra operações concorrentes.
- Caso duas operações tentem utilizar simultaneamente a última unidade de um produto, apenas uma delas poderá concluir a operação.
- O preço total da nota é calculado de acordo com os produtos e quantidades adicionados.
- A quantidade total de produtos da nota é atualizada conforme seus itens.
- Cada nota pode ser associada a um cliente.
- O usuário responsável pela operação pode ser associado à nota fiscal.
- Usuários e clientes representam conceitos diferentes dentro do sistema.
- Endereços podem ser obtidos automaticamente a partir do CEP.
- CEPs inválidos ou inexistentes não devem resultar na criação de endereços inválidos.
- Endpoints protegidos exigem autenticação através de JWT.
- Operações administrativas dependem das permissões do usuário autenticado.
- Requisições idempotentes utilizam uma chave única para impedir o processamento duplicado da mesma operação.
- Uma `Idempotency-Key` já processada não deve causar novamente os efeitos colaterais da operação original.
- Operações inválidas retornam respostas HTTP apropriadas.

## Arquitetura

O projeto foi desenvolvido como uma **aplicação backend monolítica**, organizada em camadas para separar as responsabilidades da aplicação.

```text
                         Client
                           │
                           ▼
                    Spring Security
                           │
                       JWT Filter
                           │
                           ▼
                      Controller
                           │
                           ▼
                        Service
                      ┌────┴────┐
                      │         │
                      ▼         ▼
                 Repository   Feign Client
                      │         │
                      ▼         ▼
                 PostgreSQL   ViaCEP API
```

A aplicação possui responsabilidades separadas entre diferentes componentes:

- **Controller** — recebe e processa as requisições HTTP.
- **Service** — concentra as regras de negócio.
- **Repository** — realiza o acesso aos dados utilizando Spring Data JPA.
- **Entity** — representa as entidades persistidas no banco.
- **DTO** — define os objetos utilizados para entrada e saída de dados da API.
- **Security** — realiza autenticação, validação de JWT e autorização.
- **JWT Filter** — intercepta requisições protegidas e valida os tokens recebidos.
- **Feign Client** — realiza a comunicação com serviços externos, como o ViaCEP.
- **Cache** — reduz consultas repetidas a recursos externos.
- **Exception Handler** — centraliza o tratamento de erros e exceções.

O projeto utiliza DTOs de **Request** e **Response** para separar os dados recebidos pela API das representações retornadas ao cliente.

Essa separação evita expor diretamente as entidades JPA e permite retornar informações mais adequadas, como nomes de clientes, usuários e produtos no lugar de apenas seus identificadores internos.

## Banco de dados

O projeto utiliza **PostgreSQL** como banco de dados relacional.

A persistência é realizada utilizando **Spring Data JPA** e **Hibernate**, permitindo o mapeamento entre as entidades Java e as tabelas do banco de dados.

Entre as principais entidades estão:

### `Product`

Representa os produtos cadastrados e mantém informações como código, nome, descrição, preço, estoque e categoria.

### `Invoice`

Representa uma nota fiscal e mantém informações como:

- Numeração sequencial
- Status
- Preço total
- Quantidade total de produtos
- Cliente
- Usuário responsável
- Data de criação
- Data e hora de emissão

### `InvoiceItem`

Representa cada produto pertencente a uma nota fiscal e mantém a relação entre:

- Nota fiscal
- Produto
- Quantidade

Isso permite representar corretamente diferentes quantidades de produtos dentro de uma mesma nota fiscal.

### `User`

Representa os usuários que possuem acesso ao sistema.

Mantém informações utilizadas na autenticação, incluindo credenciais e papéis utilizados pelo Spring Security.

As senhas são armazenadas utilizando hash, evitando persistência de senhas em texto puro.

### `Customer`

Representa os clientes associados às notas fiscais.

O cliente é independente do usuário autenticado no sistema, permitindo separar quem utiliza a aplicação de quem é o destinatário da nota.

### `Address`

Representa os endereços obtidos através do CEP.

As informações podem ser consultadas utilizando a integração com o ViaCEP e persistidas para reutilização.

### `IdempotencyKey`

Representa as chaves utilizadas para identificar operações idempotentes.

Permite verificar se uma determinada operação já foi processada antes de executá-la novamente.

Uma representação simplificada das relações é:

```text
User
  │
  └──────────────┐
                 │
Customer ──── Invoice
                 │
                 │ 1
                 │
                 │ N
             InvoiceItem
                 │
                 │ N
                 │
                 │ 1
              Product

Customer ───── Address

IdempotencyKey ─── Invoice
```

## API REST

A aplicação disponibiliza endpoints HTTP para gerenciamento dos recursos.

### Produtos

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/products` | Cadastra um produto |
| `GET` | `/products` | Lista os produtos |
| `GET` | `/products/{name}` | Busca produtos por nome |
| `GET` | `/products/filter/{category}` | Filtra produtos por categoria |
| `PUT` | `/products/{id}` | Atualiza um produto |
| `DELETE` | `/products/{id}` | Remove um produto |

### Notas Fiscais

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/invoices` | Cria uma nota fiscal |
| `GET` | `/invoices` | Lista as notas fiscais |
| `GET` | `/invoices/{id}/issue` | Emite e retorna os dados da nota fiscal |
| `PUT` | `/invoices/{id}/add` | Adiciona produtos à nota |
| `PUT` | `/invoices/{id}/remove` | Remove produtos da nota |
| `DELETE` | `/invoices/{id}` | Remove uma nota fiscal |

Operações configuradas como idempotentes podem exigir o header:

```text
Idempotency-Key: <chave-unica>
```

### Usuários

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/users` | Cadastra um novo usuário |

### Autenticação

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/login` | Autentica o usuário e retorna uma sessão com o token JWT |

Exemplo de autenticação:

```json
{
  "username": "usuario",
  "password": "senha"
}
```

Após a autenticação, o token recebido deve ser enviado nas rotas protegidas:

```text
Authorization: Bearer <token>
```

### Clientes

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/customers` | Cadastra um cliente |
| `GET` | `/customers` | Lista os clientes |
| `GET` | `/customers/{id}` | Busca um cliente |
| `PUT` | `/customers/{id}` | Atualiza um cliente |
| `DELETE` | `/customers/{id}` | Remove um cliente |

Durante operações de cadastro ou atualização, o CEP informado pode ser utilizado para consultar automaticamente o endereço através da integração com o ViaCEP.

> Os endpoints podem evoluir conforme novas funcionalidades forem adicionadas ao projeto.

## Segurança

A segurança da API é implementada utilizando **Spring Security** e **JWT**.

A autenticação é stateless, portanto a aplicação não mantém uma sessão HTTP tradicional no servidor.

O fluxo simplificado de autenticação é:

```text
Usuário
   │
   │ username + password
   ▼
Login
   │
   │ credenciais válidas
   ▼
Geração do JWT
   │
   ▼
Token
   │
   │ Authorization: Bearer <token>
   ▼
JWT Filter
   │
   ▼
Spring Security
   │
   ▼
Endpoint protegido
```

As permissões são definidas de acordo com os papéis dos usuários.

De forma geral:

- O cadastro de usuários pode ser acessado sem autenticação.
- Algumas consultas de produtos podem ser públicas.
- Operações protegidas exigem um JWT válido.
- Operações administrativas exigem a permissão correspondente.
- Rotas relacionadas a clientes e notas fiscais possuem regras específicas de autorização.

## Tratamento de exceções

A API possui tratamento global de exceções para transformar erros da aplicação em respostas HTTP apropriadas.

Entre os cenários tratados estão:

- Produto não encontrado
- Cliente não encontrado
- Usuário não encontrado
- Nota fiscal não encontrada
- Produto sem estoque suficiente
- Quantidade de produto inválida
- Tentativa de deixar o estoque negativo
- Tentativa de emitir uma nota fiscal que não está aberta
- Dados de entrada inválidos
- Violações das validações do Bean Validation
- Credenciais inválidas
- Usuário não autenticado
- Usuário sem autorização para determinada operação
- Token JWT inválido ou expirado
- CEP inválido
- CEP inexistente
- Falha durante a comunicação com serviços externos
- Conflitos causados por operações concorrentes
- Tentativa de processar novamente uma operação idempotente
- Operações incompatíveis com as regras de negócio

O tratamento centralizado permite manter um padrão de resposta de erro e evita espalhar lógica de tratamento de exceções pelos controllers.

## Documentação da API

A API pode ser explorada e testada utilizando **Swagger/OpenAPI**.

A documentação permite visualizar os endpoints disponíveis, parâmetros, corpos das requisições e respostas da aplicação.

Após iniciar o projeto, a interface do Swagger pode ser acessada através do endpoint configurado na aplicação.

Para endpoints protegidos, o token JWT pode ser utilizado para autenticar as requisições realizadas através da documentação da API.

## Como executar

### Pré-requisitos

Antes de executar o projeto, é necessário ter instalado:

- Java
- Maven
- PostgreSQL

Opcionalmente:

- Docker
- Docker Compose

### Configuração do banco de dados

Crie um banco PostgreSQL para a aplicação e configure as informações de conexão através das propriedades ou variáveis de ambiente utilizadas pelo projeto.

Exemplo:

```properties
spring.datasource.url=${KEY_POSTGRES_URL}
spring.datasource.username=${KEY_POSTGRES_USER}
spring.datasource.password=${KEY_POSTGRES_PASSWORD}
```

As variáveis podem ser configuradas no ambiente de execução:

```text
KEY_POSTGRES_URL=jdbc:postgresql://localhost:5432/invoice_issuance_system_db
KEY_POSTGRES_USER=postgres
KEY_POSTGRES_PASSWORD=sua_senha
```

### Configuração do JWT

A aplicação necessita de uma chave utilizada para assinatura e validação dos tokens JWT.

Exemplo:

```properties
security.config.prefix=Bearer
security.config.key=${JWT_SECRET_KEY}
security.config.expiration=3600000
```

Configure a chave através de uma variável de ambiente:

```text
JWT_SECRET_KEY=sua_chave_secreta
```

A chave real não deve ser adicionada ao repositório.

### Configuração do ViaCEP

A integração com o ViaCEP é realizada através do Spring Cloud OpenFeign.

A URL do serviço pode ser configurada nas propriedades da aplicação, de acordo com a configuração utilizada pelo projeto.

Exemplo:

```properties
viacep.url=https://viacep.com.br/ws
```

O Feign Client utiliza essa configuração para realizar as consultas de CEP.

### Cache

O projeto utiliza **Spring Cache** para otimizar consultas de endereço.

O cache é habilitado na aplicação através da configuração do Spring e utilizado nas operações de consulta de CEP para evitar chamadas externas repetidas para um mesmo endereço.

### Docker

O projeto possui configuração para execução do banco de dados utilizando Docker.

Com Docker e Docker Compose instalados, os serviços configurados podem ser iniciados utilizando:

```bash
docker compose up -d
```

As credenciais e configurações sensíveis podem ser mantidas através de variáveis de ambiente ou arquivo `.env`, que não deve ser versionado.

### Executando a aplicação

Clone o repositório:

```bash
git clone <URL_DO_REPOSITORIO>
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

O projeto utiliza **JUnit 5** e **Mockito** para testes automatizados das regras de negócio e serviços da aplicação.

Os testes incluem cenários relacionados a:

- Cadastro e gerenciamento de produtos
- Validações das regras de negócio
- Tratamento de exceções
- Operações envolvendo notas fiscais
- Atualização de estoque
- Mock de dependências utilizando Mockito

Também são utilizados testes de integração para validar cenários que dependem do comportamento conjunto da aplicação e do banco de dados.

Entre os cenários de integração está o controle de concorrência do estoque.

Por exemplo, considerando um produto com apenas uma unidade:

```text
Estoque inicial = 1

Thread A ──┐
           ├── tenta utilizar a última unidade
Thread B ──┘

Resultado esperado:
uma operação é concluída
uma operação é rejeitada
estoque final = 0
```

Esse teste permite verificar que duas operações concorrentes não conseguem consumir a mesma unidade disponível.

Para executar os testes:

```bash
./mvnw test
```

No Windows:

```bash
mvnw.cmd test
```

[//]: # (## Próximos passos)

[//]: # ()
[//]: # (Algumas funcionalidades e melhorias que podem ser incorporadas ao projeto futuramente:)

[//]: # ()
[//]: # (- [ ] Ampliar a cobertura de testes de integração)

[//]: # (- [ ] Adicionar testes específicos para autenticação e autorização)

[//]: # (- [ ] Adicionar testes para idempotência)

[//]: # (- [ ] Aprimorar a documentação da API)

[//]: # (- [ ] Implementar paginação e ordenação nas consultas)

[//]: # (- [ ] Aprimorar a estratégia de cache)

[//]: # (- [ ] Criar pipeline de CI/CD)

[//]: # (- [ ] Adicionar observabilidade e monitoramento)

[//]: # (- [ ] Aprimorar o gerenciamento de configurações por ambiente)

## Objetivo do projeto

Este projeto é utilizado como forma de estudo e prática de desenvolvimento backend com **Java e Spring Boot**, explorando a construção e evolução de uma API REST com regras de negócio próximas de cenários reais.

Durante seu desenvolvimento são aplicados conceitos como persistência com PostgreSQL, organização em camadas, DTOs, autenticação e autorização com Spring Security e JWT, integração com APIs externas utilizando OpenFeign, cache, controle de estoque, tratamento de concorrência, idempotência, tratamento global de exceções e testes automatizados.

O projeto é exclusivamente **backend** e foi desenvolvido como uma **aplicação monolítica**, sem frontend e sem arquitetura de microsserviços.

---

Desenvolvido por **Vinícius Araújo Coêlho**.