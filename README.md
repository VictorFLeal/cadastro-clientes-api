# 📋 Sistema de Cadastro de Clientes

API REST para gerenciamento de cadastro de clientes desenvolvida com Spring Boot.

Este projeto começou como um CRUD básico e foi evoluindo com base em feedbacks reais da comunidade. O objetivo é aplicar boas práticas de arquitetura, qualidade de código e testes.

---

## 🚀 Tecnologias

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL (produção) / H2 (desenvolvimento)
- Flyway
- JUnit 5 + Mockito
- OpenAPI / Swagger UI
- Maven

---

## ✅ Funcionalidades

- ✅ Cadastrar cliente
- ✅ Listar todos os clientes (com paginação)
- ✅ Buscar cliente por ID
- ✅ Buscar clientes por nome
- ✅ Atualizar dados do cliente
- ✅ Deletar cliente
- ✅ Validação de CPF (algoritmo real)
- ✅ Validação de e-mail único e CPF único
- ✅ Tratamento de erros centralizado com respostas HTTP semânticas

---

## 🔧 O que foi melhorado na v2

| Antes | Depois |
|-------|--------|
| `RuntimeException` genérica em tudo | Exceções próprias (`ClienteNotFoundException`, `CpfJaCadastradoException`) |
| HTTP 400 para qualquer erro | HTTP semântico: 404 (not found), 409 (conflict), 400 (validation) |
| DTOs com Lombok `@Data` | Java Records nativos do Java 17 |
| Hibernate criando tabelas (`ddl-auto=update`) | Flyway com scripts SQL versionados |
| Senha do banco no código | Variáveis de ambiente (`${DB_URL}`, `${DB_PASSWORD}`) |
| Zero testes | 13 cenários com JUnit 5 + Mockito |

---

## 🗂️ Estrutura do Projeto

```
src/main/java/com/seunome/cadastro_clientes/
├── controller/        # Endpoints da API
├── service/           # Regras de negócio
├── repository/        # Acesso ao banco
├── entity/            # Entidade JPA
├── dto/               # Records de entrada e saída
├── exception/         # Exceções próprias + GlobalExceptionHandler
└── validation/        # Validação customizada de CPF

src/main/resources/
├── db/migration/          # Scripts Flyway (PostgreSQL)
│   └── h2/                # Scripts Flyway (H2 - dev)
├── application.properties          # Config base
├── application-dev.properties      # Perfil dev (H2)
└── application-prod.properties     # Perfil prod (PostgreSQL)
```

---

## ▶️ Como Executar

### Pré-requisitos

- JDK 17+
- Maven

> **Não precisa do PostgreSQL instalado para rodar em desenvolvimento!**
> O perfil `dev` usa H2 em memória automaticamente.

### Rodando localmente (perfil dev)

```bash
./mvnw spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui.html`

Console H2 (banco em memória): `http://localhost:8080/h2-console`

### Rodando com PostgreSQL (perfil prod)

Configure as variáveis de ambiente antes de subir:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/cadastro_clientes
export DB_USERNAME=postgres
export DB_PASSWORD=sua_senha

./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

### Rodando os testes

```bash
./mvnw test
```

---

## 🛰️ Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/clientes` | Criar cliente |
| GET | `/api/clientes` | Listar todos (paginado) |
| GET | `/api/clientes/{id}` | Buscar por ID |
| GET | `/api/clientes/buscar?nome=` | Buscar por nome |
| PUT | `/api/clientes/{id}` | Atualizar |
| DELETE | `/api/clientes/{id}` | Deletar |

### Parâmetros de paginação

```
GET /api/clientes?page=0&size=10&sort=nome
```

---

## 📝 Exemplo de Requisição

**POST /api/clientes**

```json
{
  "nome": "João Silva",
  "cpf": "52998224725",
  "email": "joao@email.com",
  "telefone": "11987654321",
  "dataNascimento": "1990-05-15",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234567"
}
```

**Resposta HTTP 201 Created**

```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "52998224725",
  "email": "joao@email.com",
  "telefone": "11987654321",
  "dataNascimento": "1990-05-15",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234567",
  "dataCadastro": "2026-01-28T03:00:00"
}
```

### Exemplo de erro (CPF duplicado)

**Resposta HTTP 409 Conflict**

```json
{
  "timestamp": "2026-01-28T03:00:00",
  "status": 409,
  "mensagem": "CPF já cadastrado: 52998224725"
}
```

---

## 🎯 Próximos Passos

- [ ] Autenticação JWT
- [ ] Testes de integração completos
- [ ] Docker

---

## 👤 Victor Leal

Victor Leal — [GitHub](https://github.com/seu-usuario)
