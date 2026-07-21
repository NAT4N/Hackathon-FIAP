package com.fiap.hackaton.atendimento_sus.shared.exception;

/**
 * Base para todas as exceções de regra de negócio do domínio.
 * Vive na camada shared para não acoplar o domínio ao framework web.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
