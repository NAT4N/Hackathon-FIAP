CREATE TABLE paciente (
    id              UUID         PRIMARY KEY,
    nome            VARCHAR(150) NOT NULL,
    cpf             VARCHAR(11)  NOT NULL,
    data_nascimento DATE         NOT NULL,
    sexo            VARCHAR(20)  NOT NULL,
    telefone        VARCHAR(20),
    cartao_sus      VARCHAR(15),
    CONSTRAINT uk_paciente_cpf UNIQUE (cpf)
);
