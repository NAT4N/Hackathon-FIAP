package com.fiap.hackaton.atendimento_sus.triagem.application.port.out;

import com.fiap.hackaton.atendimento_sus.triagem.domain.model.NivelRisco;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.Sintoma;

import java.util.Set;

/**
 * Porta de saída para assistência de IA na triagem. É <b>best-effort</b>: o
 * classificador determinístico continua sendo a autoridade do risco; esta
 * porta apenas sugere sintomas a partir de texto livre e gera orientações.
 * Implementações devem degradar graciosamente (retorno vazio/nulo) em falha.
 */
public interface AssistenteTriagemPort {

    /** Extrai sintomas estruturados a partir de uma queixa em linguagem natural. */
    AnaliseClinica analisar(String queixaLivre);

    /** Gera um texto curto de orientação ao paciente coerente com o risco classificado. */
    String gerarOrientacao(NivelRisco nivel, Set<Sintoma> sintomas);

    /**
     * Resultado da pré-análise da queixa.
     *
     * @param sintomasSugeridos sintomas mapeados ao enum (nunca {@code null}; pode ser vazio)
     * @param resumo            breve resumo/observação da IA (pode ser {@code null})
     */
    record AnaliseClinica(Set<Sintoma> sintomasSugeridos, String resumo) {}
}
