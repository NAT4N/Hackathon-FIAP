package com.fiap.hackaton.atendimento_sus.auth.application.port.in;

import com.fiap.hackaton.atendimento_sus.auth.domain.model.Role;

import java.util.UUID;

/** Caso de uso: autenticar um usuário e emitir um token. */
public interface AutenticarUsuarioUseCase {

    Autenticacao autenticar(String email, String senha);

    record Autenticacao(String token, UUID usuarioId, String nome, Role role) {}
}
