package com.fiap.hackaton.atendimento_sus.agendamento.application.port.out;

import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Agendamento;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Porta de saída para persistência de agendamentos. */
public interface AgendamentoRepositoryPort {

    Agendamento salvar(Agendamento agendamento);

    Optional<Agendamento> buscarPorId(UUID id);

    List<Agendamento> listarPorProfissional(UUID profissionalId);

    List<Agendamento> listarPorPaciente(UUID pacienteId);

    /** Agendamentos em status ativo (AGENDADO/CONFIRMADO) do profissional — base para conflito. */
    List<Agendamento> listarAtivosDoProfissional(UUID profissionalId);
}
