package com.fiap.hackaton.atendimento_sus.shared.exception;

/** Recurso não encontrado — mapeada para HTTP 404. */
public class NotFoundException extends DomainException {

    public NotFoundException(String message) {
        super(message);
    }
}
