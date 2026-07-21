package com.fiap.hackaton.atendimento_sus.shared.exception;

/** Falha de autenticação (credenciais inválidas) — mapeada para HTTP 401. */
public class UnauthorizedException extends DomainException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
