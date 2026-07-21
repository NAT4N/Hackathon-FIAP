CREATE TABLE triagem (
    id                      UUID             PRIMARY KEY,
    paciente_id             UUID             NOT NULL,
    profissional_id         UUID             NOT NULL,
    frequencia_cardiaca     INTEGER          NOT NULL,
    frequencia_respiratoria INTEGER          NOT NULL,
    pressao_sistolica       INTEGER          NOT NULL,
    pressao_diastolica      INTEGER          NOT NULL,
    temperatura             DOUBLE PRECISION NOT NULL,
    saturacao_oxigenio      INTEGER          NOT NULL,
    escala_dor              INTEGER          NOT NULL,
    nivel_risco             VARCHAR(20)      NOT NULL,
    criado_em               TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_triagem_paciente     FOREIGN KEY (paciente_id)     REFERENCES paciente (id),
    CONSTRAINT fk_triagem_profissional FOREIGN KEY (profissional_id) REFERENCES usuario (id)
);

CREATE TABLE triagem_sintoma (
    triagem_id UUID        NOT NULL,
    sintoma    VARCHAR(30) NOT NULL,
    CONSTRAINT fk_triagem_sintoma FOREIGN KEY (triagem_id) REFERENCES triagem (id)
);

CREATE INDEX idx_triagem_paciente ON triagem (paciente_id);
