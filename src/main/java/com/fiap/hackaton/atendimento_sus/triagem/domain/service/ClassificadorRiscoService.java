package com.fiap.hackaton.atendimento_sus.triagem.domain.service;

import com.fiap.hackaton.atendimento_sus.triagem.domain.model.NivelRisco;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.SinaisVitais;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.Sintoma;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;

/**
 * Serviço de domínio puro (sem framework) que classifica o risco no estilo
 * Protocolo de Manchester. As regras são avaliadas em ordem decrescente de
 * gravidade — o <b>primeiro critério satisfeito vence</b>. Se nada casar, o
 * paciente é classificado como AZUL (não urgente).
 */
public class ClassificadorRiscoService {

    /** Uma regra associa um predicado sobre o quadro clínico a um nível de risco. */
    private record Regra(NivelRisco nivel, BiPredicate<SinaisVitais, Set<Sintoma>> criterio) {}

    private static final List<Regra> REGRAS = List.of(
            // VERMELHO — risco de vida imediato
            new Regra(NivelRisco.VERMELHO, (sv, s) ->
                    sv.saturacaoOxigenio() < 85
                            || s.contains(Sintoma.PERDA_DE_CONSCIENCIA)
                            || s.contains(Sintoma.CONVULSAO)
                            || sv.frequenciaRespiratoria() < 8 || sv.frequenciaRespiratoria() > 36
                            || sv.pressaoSistolica() < 80
                            || sv.frequenciaCardiaca() < 40 || sv.frequenciaCardiaca() > 150),

            // LARANJA — muito urgente
            new Regra(NivelRisco.LARANJA, (sv, s) ->
                    sv.saturacaoOxigenio() < 92
                            || s.contains(Sintoma.DOR_TORACICA)
                            || s.contains(Sintoma.FALTA_DE_AR)
                            || s.contains(Sintoma.SANGRAMENTO_INTENSO)
                            || sv.escalaDor() >= 8
                            || sv.temperatura() >= 40.0
                            || sv.pressaoSistolica() >= 200 || sv.pressaoSistolica() < 90
                            || sv.frequenciaCardiaca() > 130),

            // AMARELO — urgente
            new Regra(NivelRisco.AMARELO, (sv, s) ->
                    sv.escalaDor() >= 5
                            || sv.temperatura() >= 38.5
                            || sv.saturacaoOxigenio() < 95
                            || sv.frequenciaCardiaca() > 110
                            || (s.contains(Sintoma.FEBRE)
                                && (s.contains(Sintoma.VOMITO) || s.contains(Sintoma.DOR_ABDOMINAL)))),

            // VERDE — pouco urgente
            new Regra(NivelRisco.VERDE, (sv, s) ->
                    sv.escalaDor() >= 2
                            || sv.temperatura() >= 37.8
                            || !s.isEmpty()));

    /**
     * Classifica o risco a partir dos sinais vitais e dos sintomas relatados.
     *
     * @return o nível de risco; nunca {@code null}.
     */
    public NivelRisco classificar(SinaisVitais sinaisVitais, Set<Sintoma> sintomas) {
        if (sinaisVitais == null) {
            throw new IllegalArgumentException("Sinais vitais são obrigatórios para classificar");
        }
        Set<Sintoma> sintomasSeguro = sintomas == null ? EnumSet.noneOf(Sintoma.class) : sintomas;
        return REGRAS.stream()
                .filter(regra -> regra.criterio().test(sinaisVitais, sintomasSeguro))
                .map(Regra::nivel)
                .findFirst()
                .orElse(NivelRisco.AZUL);
    }
}
