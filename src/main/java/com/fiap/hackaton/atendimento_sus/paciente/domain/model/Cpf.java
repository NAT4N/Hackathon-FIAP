package com.fiap.hackaton.atendimento_sus.paciente.domain.model;

import com.fiap.hackaton.atendimento_sus.shared.exception.BusinessRuleException;

/**
 * Value object de CPF. Armazena apenas os 11 dígitos e valida os dígitos
 * verificadores (algoritmo oficial da Receita Federal).
 */
public record Cpf(String numero) {

    public Cpf {
        if (numero == null) {
            throw new BusinessRuleException("CPF é obrigatório");
        }
        numero = numero.replaceAll("\\D", "");
        if (numero.length() != 11) {
            throw new BusinessRuleException("CPF deve conter 11 dígitos");
        }
        if (!digitosVerificadoresValidos(numero)) {
            throw new BusinessRuleException("CPF inválido");
        }
    }

    private static boolean digitosVerificadoresValidos(String cpf) {
        // Rejeita sequências repetidas (00000000000, 11111111111, ...).
        if (cpf.chars().distinct().count() == 1) {
            return false;
        }
        int primeiro = calcularDigito(cpf, 9, 10);
        int segundo = calcularDigito(cpf, 10, 11);
        return primeiro == (cpf.charAt(9) - '0') && segundo == (cpf.charAt(10) - '0');
    }

    private static int calcularDigito(String cpf, int tamanho, int pesoInicial) {
        int soma = 0;
        int peso = pesoInicial;
        for (int i = 0; i < tamanho; i++) {
            soma += (cpf.charAt(i) - '0') * peso--;
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    /** Retorna o CPF formatado (000.000.000-00). */
    public String formatado() {
        return numero.substring(0, 3) + "." + numero.substring(3, 6) + "."
                + numero.substring(6, 9) + "-" + numero.substring(9);
    }
}
