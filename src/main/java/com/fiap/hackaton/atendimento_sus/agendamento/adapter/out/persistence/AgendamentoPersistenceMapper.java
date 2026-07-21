package com.fiap.hackaton.atendimento_sus.agendamento.adapter.out.persistence;

import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Agendamento;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.JanelaHorario;

final class AgendamentoPersistenceMapper {

    private AgendamentoPersistenceMapper() {
    }

    static AgendamentoJpaEntity toEntity(Agendamento a) {
        return new AgendamentoJpaEntity(
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
                a.getStatus(),
                a.getCriadoEm());
    }

    static Agendamento toDomain(AgendamentoJpaEntity e) {
        return Agendamento.reconstituir(
                e.getId(),
                e.getPacienteId(),
                e.getProfissionalId(),
                e.getTipo(),
                e.getEspecialidade(),
                new JanelaHorario(e.getInicio(), e.getFim()),
                e.getTriagemId(),
                e.getObservacao(),
                e.getPrioridade(),
                e.getStatus(),
                e.getCriadoEm());
    }
}
