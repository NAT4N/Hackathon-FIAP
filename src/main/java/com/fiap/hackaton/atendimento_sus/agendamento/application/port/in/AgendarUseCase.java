package com.fiap.hackaton.atendimento_sus.agendamento.application.port.in;

import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Agendamento;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.TipoAtendimento;

import java.time.LocalDateTime;
import java.util.UUID;

/** Caso de uso: agendar consulta ou exame. */
public interface AgendarUseCase {

    Agendamento agendar(AgendarCommand command);

    /**
     * @param triagemId opcional; quando presente, a prioridade é derivada do
     *                  nível de risco da triagem (senão, ROTINA).
     */
    record AgendarCommand(
            UUID pacienteId,
            UUID profissionalId,
            TipoAtendimento tipo,
            String especialidade,
            LocalDateTime inicio,
            int duracaoMinutos,
            UUID triagemId,
            String observacao) {}
}
