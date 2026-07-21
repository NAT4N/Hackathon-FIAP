package com.fiap.hackaton.atendimento_sus.agendamento.application.port.out;

import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Prioridade;

import java.util.UUID;

/**
 * Costura de integração triagem→agendamento (anti-corruption layer). O
 * agendamento conhece apenas sua própria {@link Prioridade}; a tradução a
 * partir do nível de risco da triagem acontece no adapter que implementa esta
 * porta, isolando o domínio de agendamento do domínio de triagem.
 */
public interface ClassificacaoRiscoPort {

    Prioridade buscarPrioridade(UUID triagemId);
}
