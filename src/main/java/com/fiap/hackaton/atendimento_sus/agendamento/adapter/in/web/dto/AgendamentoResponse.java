package com.fiap.hackaton.atendimento_sus.agendamento.adapter.in.web.dto;

import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Agendamento;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Prioridade;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.StatusAgendamento;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.TipoAtendimento;

import java.time.LocalDateTime;
import java.util.UUID;

public record AgendamentoResponse(
        UUID id,
        UUID pacienteId,
        UUID profissionalId,
        TipoAtendimento tipo,
        String especialidade,
        LocalDateTime inicio,
        LocalDateTime fim,
        UUID triagemId,
        String observacao,
        Prioridade prioridade,
        StatusAgendamento status) {

    public static AgendamentoResponse de(Agendamento a) {
        return new AgendamentoResponse(
                a.getId(),
                a.getPacienteId(),
                a.getProfissionalId(),
                a.getTipo(),
                a.getEspecialidade(),
                a.getJanela().inicio(),
                a.getJanela().fim(),
                a.getTriagemId(),
                a.getObservacao(),
                a.getPrioridade(),
                a.getStatus());
    }
}
