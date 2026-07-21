package com.fiap.hackaton.atendimento_sus.triagem.domain.service;

import com.fiap.hackaton.atendimento_sus.triagem.domain.model.NivelRisco;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.SinaisVitais;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.Sintoma;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ClassificadorRiscoServiceTest {

    private final ClassificadorRiscoService classificador = new ClassificadorRiscoService();

    /** Sinais vitais normais de um adulto saudável, sem sintomas relevantes. */
    private SinaisVitais normais() {
        return new SinaisVitais(75, 16, 120, 80, 36.5, 98, 0);
    }

    @Test
    @DisplayName("Saturação criticamente baixa classifica como VERMELHO")
    void saturacaoCriticaEhVermelho() {
        SinaisVitais sv = new SinaisVitais(75, 16, 120, 80, 36.5, 80, 0);
        assertThat(classificador.classificar(sv, Set.of())).isEqualTo(NivelRisco.VERMELHO);
    }

    @Test
    @DisplayName("Perda de consciência classifica como VERMELHO mesmo com sinais normais")
    void perdaDeConscienciaEhVermelho() {
        assertThat(classificador.classificar(normais(), Set.of(Sintoma.PERDA_DE_CONSCIENCIA)))
                .isEqualTo(NivelRisco.VERMELHO);
    }

    @Test
    @DisplayName("Dor torácica com sinais normais classifica como LARANJA")
    void dorToracicaEhLaranja() {
        assertThat(classificador.classificar(normais(), Set.of(Sintoma.DOR_TORACICA)))
                .isEqualTo(NivelRisco.LARANJA);
    }

    @Test
    @DisplayName("A regra mais grave vence: saturação crítica + dor torácica → VERMELHO")
    void primeiroCriterioVence() {
        SinaisVitais sv = new SinaisVitais(75, 16, 120, 80, 36.5, 80, 0);
        assertThat(classificador.classificar(sv, Set.of(Sintoma.DOR_TORACICA)))
                .isEqualTo(NivelRisco.VERMELHO);
    }

    @Test
    @DisplayName("Escala de dor moderada (5) classifica como AMARELO")
    void dorModeradaEhAmarelo() {
        SinaisVitais sv = new SinaisVitais(75, 16, 120, 80, 36.5, 98, 5);
        assertThat(classificador.classificar(sv, Set.of())).isEqualTo(NivelRisco.AMARELO);
    }

    @Test
    @DisplayName("Febre com vômito classifica como AMARELO")
    void febreComVomitoEhAmarelo() {
        assertThat(classificador.classificar(normais(), EnumSet.of(Sintoma.FEBRE, Sintoma.VOMITO)))
                .isEqualTo(NivelRisco.AMARELO);
    }

    @Test
    @DisplayName("Sintoma leve isolado classifica como VERDE")
    void sintomaLeveEhVerde() {
        assertThat(classificador.classificar(normais(), Set.of(Sintoma.TOSSE)))
                .isEqualTo(NivelRisco.VERDE);
    }

    @Test
    @DisplayName("Sem alterações classifica como AZUL")
    void semAlteracoesEhAzul() {
        assertThat(classificador.classificar(normais(), Set.of())).isEqualTo(NivelRisco.AZUL);
    }

    @Test
    @DisplayName("Sintomas nulos são tratados como conjunto vazio")
    void sintomasNulosNaoQuebram() {
        assertThat(classificador.classificar(normais(), null)).isEqualTo(NivelRisco.AZUL);
    }
}
