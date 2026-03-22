-- V1__create_table_clientes.sql
-- Criação da tabela de clientes com constraints explícitas no banco

CREATE TABLE clientes (
    id               BIGSERIAL       PRIMARY KEY,
    nome             VARCHAR(100)    NOT NULL,
    cpf              CHAR(11)        NOT NULL,
    email            VARCHAR(150)    NOT NULL,
    telefone         VARCHAR(11),
    data_nascimento  DATE            NOT NULL,
    endereco         VARCHAR(255),
    cidade           VARCHAR(100),
    estado           CHAR(2),
    cep              CHAR(8),
    data_cadastro    TIMESTAMP       NOT NULL DEFAULT NOW(),
    data_atualizacao TIMESTAMP,

    -- Constraints de unicidade no banco (além da validação na aplicação)
    CONSTRAINT uq_clientes_cpf   UNIQUE (cpf),
    CONSTRAINT uq_clientes_email UNIQUE (email),

    -- Constraint de formato básico no banco
    CONSTRAINT chk_clientes_cpf CHECK (cpf ~ '^\d{11}$'),
    CONSTRAINT chk_clientes_cep CHECK (cep IS NULL OR cep ~ '^\d{8}$')
);

-- Índices para buscas frequentes
CREATE INDEX idx_clientes_nome  ON clientes (nome);
CREATE INDEX idx_clientes_email ON clientes (email);
CREATE INDEX idx_clientes_cpf   ON clientes (cpf);
