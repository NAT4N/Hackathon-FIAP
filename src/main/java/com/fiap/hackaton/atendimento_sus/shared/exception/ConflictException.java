package com.fiap.hackaton.atendimento_sus.shared.exception;

/** Conflito com o estado atual do recurso — mapeada para HTTP 409. */
public class ConflictException extends DomainException {

    public ConflictException(String message) {
        super(message);
    }
}
