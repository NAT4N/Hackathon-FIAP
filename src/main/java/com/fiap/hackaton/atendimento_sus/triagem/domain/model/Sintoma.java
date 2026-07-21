package com.fiap.hackaton.atendimento_sus.triagem.domain.model;

/**
 * Sintomas considerados na classificação de risco. Alguns são "bandeiras
 * vermelhas" que, isoladamente, já elevam o nível de risco (ver
 * {@code ClassificadorRiscoService}).
 */
public enum Sintoma {
    DOR_TORACICA,
    FALTA_DE_AR,
    SANGRAMENTO_INTENSO,
    PERDA_DE_CONSCIENCIA,
    CONVULSAO,
    DOR_ABDOMINAL,
    CEFALEIA,
    FEBRE,
    VOMITO,
    TOSSE,
    DOR_LEVE,
    MAL_ESTAR
}
