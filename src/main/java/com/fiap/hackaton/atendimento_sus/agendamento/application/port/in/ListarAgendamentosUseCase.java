package com.fiap.hackaton.atendimento_sus.agendamento.application.port.in;

import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Agendamento;

import java.util.List;
import java.util.UUID;

/** Caso de uso: listar agendamentos por profissional ou por paciente. */
public interface ListarAgendamentosUseCase {

    List<Agendamento> listarPorProfissional(UUID profissionalId);

    List<Agendamento> listarPorPaciente(UUID pacienteId);
}
