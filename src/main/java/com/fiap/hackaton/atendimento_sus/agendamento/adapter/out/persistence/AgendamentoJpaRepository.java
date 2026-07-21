package com.fiap.hackaton.atendimento_sus.agendamento.adapter.out.persistence;

import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface AgendamentoJpaRepository extends JpaRepository<AgendamentoJpaEntity, UUID> {

    List<AgendamentoJpaEntity> findByProfissionalId(UUID profissionalId);

    List<AgendamentoJpaEntity> findByPacienteId(UUID pacienteId);

    List<AgendamentoJpaEntity> findByProfissionalIdAndStatusIn(UUID profissionalId, Collection<StatusAgendamento> status);
}
