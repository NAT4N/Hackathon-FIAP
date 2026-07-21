package com.fiap.hackaton.atendimento_sus.agendamento.adapter.out.persistence;

import com.fiap.hackaton.atendimento_sus.agendamento.application.port.out.AgendamentoRepositoryPort;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Agendamento;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.StatusAgendamento;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AgendamentoPersistenceAdapter implements AgendamentoRepositoryPort {

    private static final List<StatusAgendamento> ATIVOS =
            List.of(StatusAgendamento.AGENDADO, StatusAgendamento.CONFIRMADO);

    private final AgendamentoJpaRepository repository;

    public AgendamentoPersistenceAdapter(AgendamentoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Agendamento salvar(Agendamento agendamento) {
        return AgendamentoPersistenceMapper.toDomain(
                repository.save(AgendamentoPersistenceMapper.toEntity(agendamento)));
    }

    @Override
    public Optional<Agendamento> buscarPorId(UUID id) {
        return repository.findById(id).map(AgendamentoPersistenceMapper::toDomain);
    }

    @Override
    public List<Agendamento> listarPorProfissional(UUID profissionalId) {
        return repository.findByProfissionalId(profissionalId).stream()
                .map(AgendamentoPersistenceMapper::toDomain).toList();
    }

    @Override
    public List<Agendamento> listarPorPaciente(UUID pacienteId) {
        return repository.findByPacienteId(pacienteId).stream()
                .map(AgendamentoPersistenceMapper::toDomain).toList();
    }

    @Override
    public List<Agendamento> listarAtivosDoProfissional(UUID profissionalId) {
        return repository.findByProfissionalIdAndStatusIn(profissionalId, ATIVOS).stream()
                .map(AgendamentoPersistenceMapper::toDomain).toList();
    }
}
