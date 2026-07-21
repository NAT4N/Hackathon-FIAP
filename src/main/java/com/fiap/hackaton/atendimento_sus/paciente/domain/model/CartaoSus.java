package com.fiap.hackaton.atendimento_sus.paciente.domain.model;

import com.fiap.hackaton.atendimento_sus.shared.exception.BusinessRuleException;

/** Value object do número do Cartão Nacional de Saúde (CNS): 15 dígitos. */
public record CartaoSus(String numero) {

    public CartaoSus {
        if (numero == null) {
            throw new BusinessRuleException("Cartão SUS é obrigatório");
        }
        numero = numero.replaceAll("\\D", "");
        if (numero.length() != 15) {
            throw new BusinessRuleException("Cartão SUS deve conter 15 dígitos");
        }
    }
}
