package com.fiap.hackaton.atendimento_sus.shared.exception;

/** Violação de regra de negócio — mapeada para HTTP 422 (Unprocessable Entity). */
public class BusinessRuleException extends DomainException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
