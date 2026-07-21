CREATE TABLE agendamento (
    id              UUID         PRIMARY KEY,
    paciente_id     UUID         NOT NULL,
    profissional_id UUID         NOT NULL,
    tipo            VARCHAR(20)  NOT NULL,
    especialidade   VARCHAR(120),
    inicio          TIMESTAMP    NOT NULL,
    fim             TIMESTAMP    NOT NULL,
    triagem_id      UUID,
    observacao      VARCHAR(500),
    prioridade      VARCHAR(20)  NOT NULL,
    status          VARCHAR(20)  NOT NULL,
    criado_em       TIMESTAMP    NOT NULL,
    CONSTRAINT fk_agendamento_paciente     FOREIGN KEY (paciente_id)     REFERENCES paciente (id),
    CONSTRAINT fk_agendamento_profissional FOREIGN KEY (profissional_id) REFERENCES usuario (id),
    CONSTRAINT fk_agendamento_triagem      FOREIGN KEY (triagem_id)      REFERENCES triagem (id)
);

CREATE INDEX idx_agendamento_profissional_inicio ON agendamento (profissional_id, inicio);
CREATE INDEX idx_agendamento_paciente ON agendamento (paciente_id);
