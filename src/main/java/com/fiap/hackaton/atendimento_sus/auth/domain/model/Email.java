package com.fiap.hackaton.atendimento_sus.auth.domain.model;

import com.fiap.hackaton.atendimento_sus.shared.exception.BusinessRuleException;

import java.util.regex.Pattern;

/** Value object de e-mail, normalizado (lowercase/trim) e validado. */
public record Email(String valor) {

    private static final Pattern PADRAO =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        if (valor == null || valor.isBlank()) {
            throw new BusinessRuleException("E-mail é obrigatório");
        }
        valor = valor.trim().toLowerCase();
        if (!PADRAO.matcher(valor).matches()) {
            throw new BusinessRuleException("E-mail inválido: " + valor);
        }
    }
}
