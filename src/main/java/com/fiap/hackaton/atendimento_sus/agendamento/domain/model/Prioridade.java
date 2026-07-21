package com.fiap.hackaton.atendimento_sus.agendamento.domain.model;

/**
 * Prioridade do agendamento — conceito <b>próprio</b> do contexto de
 * agendamento (o domínio de agendamento nunca importa {@code NivelRisco} da
 * triagem). A conversão risco→prioridade acontece no adapter ACL. Quando não
 * há triagem associada, usa-se {@link #ROTINA}.
 */
public enum Prioridade {

    EMERGENCIA(1),
    MUITO_URGENTE(2),
    URGENTE(3),
    POUCO_URGENTE(4),
    NAO_URGENTE(5),
    ROTINA(6);

    private final int peso;

    Prioridade(int peso) {
        this.peso = peso;
    }

    /** Menor peso = maior prioridade de atendimento. */
    public int peso() {
        return peso;
    }
}
