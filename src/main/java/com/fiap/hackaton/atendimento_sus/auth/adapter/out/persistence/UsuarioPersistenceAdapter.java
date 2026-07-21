package com.fiap.hackaton.atendimento_sus.auth.adapter.out.persistence;

import com.fiap.hackaton.atendimento_sus.auth.application.port.out.UsuarioRepositoryPort;
import com.fiap.hackaton.atendimento_sus.auth.domain.model.Email;
import com.fiap.hackaton.atendimento_sus.auth.domain.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository repository;

    public UsuarioPersistenceAdapter(UsuarioJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        return UsuarioPersistenceMapper.toDomain(
                repository.save(UsuarioPersistenceMapper.toEntity(usuario)));
    }

    @Override
    public Optional<Usuario> buscarPorEmail(Email email) {
        return repository.findByEmail(email.valor()).map(UsuarioPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return repository.findById(id).map(UsuarioPersistenceMapper::toDomain);
    }

    @Override
    public boolean existePorEmail(Email email) {
        return repository.existsByEmail(email.valor());
    }
}
