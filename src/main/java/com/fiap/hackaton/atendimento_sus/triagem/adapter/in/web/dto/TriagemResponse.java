package com.fiap.hackaton.atendimento_sus.triagem.adapter.in.web.dto;

import com.fiap.hackaton.atendimento_sus.triagem.domain.model.NivelRisco;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.Sintoma;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.Triagem;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record TriagemResponse(
        UUID id,
        UUID pacienteId,
        UUID profissionalId,
        NivelRisco nivelRisco,
        String descricaoRisco,
        int tempoAlvoMinutos,
        Set<Sintoma> sintomas,
        String orientacao,
        Instant criadoEm) {

    public static TriagemResponse de(Triagem t) {
        return new TriagemResponse(
                t.getId(),
                t.getPacienteId(),
                t.getProfissionalId(),
                t.getNivelRisco(),
                t.getNivelRisco().descricao(),
                t.getNivelRisco().tempoAlvoMinutos(),
                t.getSintomas(),
                t.getOrientacao(),
                t.getCriadoEm());
    }
}
