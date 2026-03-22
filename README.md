# 📋 Sistema de Cadastro de Clientes

API REST para gerenciamento de cadastro de clientes desenvolvida com Spring Boot.

## 🚀 Tecnologias

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok

## 📌 Funcionalidades

- ✅ Cadastrar cliente
- ✅ Listar todos os clientes (com paginação)
- ✅ Buscar cliente por ID
- ✅ Buscar clientes por nome
- ✅ Atualizar dados do cliente
- ✅ Deletar cliente
- ✅ Validações de CPF e email únicos

## 🔧 Como Executar

### Pré-requisitos

- JDK 17+
- PostgreSQL
- Maven

### Configuração do Banco
```sql
CREATE DATABASE cadastro_clientes;
```

### Executar Aplicação
```bash
./mvnw spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

## 📖 Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/clientes` | Criar cliente |
| GET | `/api/clientes` | Listar todos |
| GET | `/api/clientes/{id}` | Buscar por ID |
| GET | `/api/clientes/buscar?nome=` | Buscar por nome |
| PUT | `/api/clientes/{id}` | Atualizar |
| DELETE | `/api/clientes/{id}` | Deletar |

## 📝 Exemplo de Requisição
```json
{
  "nome": "João Silva",
  "cpf": "12345678901",
  "email": "joao@email.com",
  "telefone": "11987654321",
  "dataNascimento": "1990-05-15",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234567"
}
```

## 🎯 Melhorias Futuras

- [ ] Autenticação JWT
- [ ] Documentação Swagger
- [ ] Validação customizada de CPF
- [ ] Testes unitários
- [ ] Docker

## 👤 Victor Leal

**Victor Leal** - [GitHub](https://github.com/VictorFLeal)
