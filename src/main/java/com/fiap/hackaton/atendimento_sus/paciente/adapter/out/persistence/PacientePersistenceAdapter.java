package com.fiap.hackaton.atendimento_sus.paciente.adapter.out.persistence;

import com.fiap.hackaton.atendimento_sus.paciente.application.port.out.PacienteRepositoryPort;
import com.fiap.hackaton.atendimento_sus.paciente.domain.model.Cpf;
import com.fiap.hackaton.atendimento_sus.paciente.domain.model.Paciente;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PacientePersistenceAdapter implements PacienteRepositoryPort {

    private final PacienteJpaRepository repository;

    public PacientePersistenceAdapter(PacienteJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Paciente salvar(Paciente paciente) {
        return PacientePersistenceMapper.toDomain(
                repository.save(PacientePersistenceMapper.toEntity(paciente)));
    }

    @Override
    public Optional<Paciente> buscarPorId(UUID id) {
        return repository.findById(id).map(PacientePersistenceMapper::toDomain);
    }

    @Override
    public List<Paciente> listar() {
        return repository.findAll().stream().map(PacientePersistenceMapper::toDomain).toList();
    }

    @Override
    public boolean existePorCpf(Cpf cpf) {
        return repository.existsByCpf(cpf.numero());
    }
}
