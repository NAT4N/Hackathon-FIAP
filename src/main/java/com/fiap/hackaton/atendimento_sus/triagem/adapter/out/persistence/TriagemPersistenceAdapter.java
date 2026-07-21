package com.fiap.hackaton.atendimento_sus.triagem.adapter.out.persistence;

import com.fiap.hackaton.atendimento_sus.triagem.application.port.out.TriagemRepositoryPort;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.Triagem;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TriagemPersistenceAdapter implements TriagemRepositoryPort {

    private final TriagemJpaRepository repository;

    public TriagemPersistenceAdapter(TriagemJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Triagem salvar(Triagem triagem) {
        return TriagemPersistenceMapper.toDomain(
                repository.save(TriagemPersistenceMapper.toEntity(triagem)));
    }

    @Override
    public Optional<Triagem> buscarPorId(UUID id) {
        return repository.findById(id).map(TriagemPersistenceMapper::toDomain);
    }
}
