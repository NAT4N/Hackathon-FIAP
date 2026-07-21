package com.fiap.hackaton.atendimento_sus.triagem.application.port.in;

import com.fiap.hackaton.atendimento_sus.triagem.domain.model.Triagem;

import java.util.UUID;

/** Caso de uso: consultar uma triagem por id. */
public interface ConsultarTriagemUseCase {

    Triagem buscarPorId(UUID id);
}
