package com.fiap.hackaton.atendimento_sus.triagem.application.port.in;

import com.fiap.hackaton.atendimento_sus.triagem.domain.model.SinaisVitais;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.Sintoma;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.Triagem;

import java.util.Set;
import java.util.UUID;

/** Caso de uso: realizar (classificar e registrar) uma triagem. */
public interface RealizarTriagemUseCase {

    Triagem realizar(RealizarTriagemCommand command);

    record RealizarTriagemCommand(
            UUID pacienteId,
            UUID profissionalId,
            SinaisVitais sinaisVitais,
            Set<Sintoma> sintomas) {}
}
