package com.fiap.hackaton.atendimento_sus.shared.security;

import java.util.Optional;

/** Porta para validação de tokens de acesso, usada pelo filtro de segurança. */
public interface TokenValidatorPort {

    /** Valida o token e devolve o principal, ou {@code Optional.empty()} se inválido/expirado. */
    Optional<AuthenticatedUser> validar(String token);
}
