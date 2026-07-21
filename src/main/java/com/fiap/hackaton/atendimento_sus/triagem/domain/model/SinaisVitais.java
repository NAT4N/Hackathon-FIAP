package com.fiap.hackaton.atendimento_sus.triagem.domain.model;

import com.fiap.hackaton.atendimento_sus.shared.exception.BusinessRuleException;

/**
 * Value object com os sinais vitais aferidos na triagem. Valida faixas
 * fisiologicamente plausíveis na construção.
 */
public record SinaisVitais(
        int frequenciaCardiaca,      // bpm
        int frequenciaRespiratoria,  // irpm
        int pressaoSistolica,        // mmHg
        int pressaoDiastolica,       // mmHg
        double temperatura,          // °C
        int saturacaoOxigenio,       // %
        int escalaDor) {             // 0-10

    public SinaisVitais {
        exigirFaixa("Frequência cardíaca", frequenciaCardiaca, 10, 300);
        exigirFaixa("Frequência respiratória", frequenciaRespiratoria, 3, 80);
        exigirFaixa("Pressão sistólica", pressaoSistolica, 40, 300);
        exigirFaixa("Pressão diastólica", pressaoDiastolica, 20, 200);
        exigirFaixa("Temperatura", temperatura, 25.0, 45.0);
        exigirFaixa("Saturação de oxigênio", saturacaoOxigenio, 30, 100);
        exigirFaixa("Escala de dor", escalaDor, 0, 10);
        if (pressaoDiastolica > pressaoSistolica) {
            throw new BusinessRuleException("Pressão diastólica não pode superar a sistólica");
        }
    }

    private static void exigirFaixa(String campo, double valor, double min, double max) {
        if (valor < min || valor > max) {
            throw new BusinessRuleException(
                    "%s fora da faixa plausível (%s a %s): %s".formatted(campo, min, max, valor));
        }
    }
}
