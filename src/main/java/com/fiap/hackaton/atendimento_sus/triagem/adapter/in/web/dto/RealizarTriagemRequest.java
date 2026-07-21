package com.fiap.hackaton.atendimento_sus.triagem.adapter.in.web.dto;

import com.fiap.hackaton.atendimento_sus.triagem.domain.model.Sintoma;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

/** Sinais vitais e sintomas informados na triagem. As faixas são validadas no VO de domínio. */
public record RealizarTriagemRequest(
        @NotNull UUID pacienteId,
        @NotNull Integer frequenciaCardiaca,
        @NotNull Integer frequenciaRespiratoria,
        @NotNull Integer pressaoSistolica,
        @NotNull Integer pressaoDiastolica,
        @NotNull Double temperatura,
        @NotNull Integer saturacaoOxigenio,
        @NotNull Integer escalaDor,
        Set<Sintoma> sintomas) {
}
