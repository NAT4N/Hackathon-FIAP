package com.fiap.hackaton.atendimento_sus.agendamento.domain.model;

import java.util.Set;

/**
 * Estados do ciclo de vida de um agendamento e as transições permitidas.
 * <pre>
 *   AGENDADO   → CONFIRMADO | CANCELADO | NAO_COMPARECEU
 *   CONFIRMADO → REALIZADO  | CANCELADO | NAO_COMPARECEU
 *   REALIZADO / CANCELADO / NAO_COMPARECEU → (terminais)
 * </pre>
 */
public enum StatusAgendamento {

    AGENDADO,
    CONFIRMADO,
    REALIZADO,
    CANCELADO,
    NAO_COMPARECEU;

    /** Estados considerados "ativos" para efeito de detecção de conflito de agenda. */
    public boolean ativo() {
        return this == AGENDADO || this == CONFIRMADO;
    }

    public boolean podeTransicionarPara(StatusAgendamento destino) {
        return switch (this) {
            case AGENDADO -> Set.of(CONFIRMADO, CANCELADO, NAO_COMPARECEU).contains(destino);
            case CONFIRMADO -> Set.of(REALIZADO, CANCELADO, NAO_COMPARECEU).contains(destino);
            case REALIZADO, CANCELADO, NAO_COMPARECEU -> false;
        };
    }
}
