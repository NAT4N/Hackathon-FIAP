package com.fiap.hackaton.atendimento_sus.agendamento.application.port.in;

import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Agendamento;

import java.util.UUID;

/** Casos de uso de transição de estado de um agendamento existente. */
public interface GerenciarAgendamentoUseCase {

    Agendamento confirmar(UUID agendamentoId);

    Agendamento cancelar(UUID agendamentoId);

    Agendamento concluir(UUID agendamentoId);
}
