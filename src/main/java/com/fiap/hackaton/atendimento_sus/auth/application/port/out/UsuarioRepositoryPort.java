package com.fiap.hackaton.atendimento_sus.auth.application.port.out;

import com.fiap.hackaton.atendimento_sus.auth.domain.model.Email;
import com.fiap.hackaton.atendimento_sus.auth.domain.model.Usuario;

import java.util.Optional;
import java.util.UUID;

/** Porta de saída para persistência de usuários. */
public interface UsuarioRepositoryPort {

    Usuario salvar(Usuario usuario);

    Optional<Usuario> buscarPorEmail(Email email);

    Optional<Usuario> buscarPorId(UUID id);

    boolean existePorEmail(Email email);
}
