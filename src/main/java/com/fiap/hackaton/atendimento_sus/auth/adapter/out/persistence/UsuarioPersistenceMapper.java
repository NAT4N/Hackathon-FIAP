package com.fiap.hackaton.atendimento_sus.auth.adapter.out.persistence;

import com.fiap.hackaton.atendimento_sus.auth.domain.model.Email;
import com.fiap.hackaton.atendimento_sus.auth.domain.model.Usuario;

/** Converte entre {@link Usuario} (domínio) e {@link UsuarioJpaEntity} (persistência). */
final class UsuarioPersistenceMapper {

    private UsuarioPersistenceMapper() {
    }

    static UsuarioJpaEntity toEntity(Usuario usuario) {
        return new UsuarioJpaEntity(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail().valor(),
                usuario.getSenhaHash(),
                usuario.getRole(),
                usuario.isAtivo());
    }

    static Usuario toDomain(UsuarioJpaEntity entity) {
        return Usuario.reconstituir(
                entity.getId(),
                entity.getNome(),
                new Email(entity.getEmail()),
                entity.getSenhaHash(),
                entity.getRole(),
                entity.isAtivo());
    }
}
