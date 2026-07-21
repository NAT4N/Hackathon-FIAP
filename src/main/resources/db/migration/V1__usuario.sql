CREATE TABLE usuario (
    id         UUID         PRIMARY KEY,
    nome       VARCHAR(150) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    role       VARCHAR(20)  NOT NULL,
    ativo      BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_usuario_email UNIQUE (email)
);
