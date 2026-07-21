package com.fiap.hackaton.atendimento_sus.agendamento.domain.model;

import com.fiap.hackaton.atendimento_sus.shared.exception.BusinessRuleException;

import java.time.LocalDateTime;

/** Value object de intervalo de tempo [inicio, fim) com teste de sobreposição. */
public record JanelaHorario(LocalDateTime inicio, LocalDateTime fim) {

    public JanelaHorario {
        if (inicio == null || fim == null) {
            throw new BusinessRuleException("Início e fim da janela são obrigatórios");
        }
        if (!fim.isAfter(inicio)) {
            throw new BusinessRuleException("O fim da janela deve ser posterior ao início");
        }
    }

    /** Verdadeiro se este intervalo sobrepõe {@code outra} (tratando [inicio, fim)). */
    public boolean sobrepoe(JanelaHorario outra) {
        return inicio.isBefore(outra.fim) && outra.inicio.isBefore(fim);
    }
}
