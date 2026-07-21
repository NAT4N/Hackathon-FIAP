package com.fiap.hackaton.atendimento_sus.triagem.application.port.out;

import com.fiap.hackaton.atendimento_sus.triagem.domain.model.Triagem;

import java.util.Optional;
import java.util.UUID;

/** Porta de saída para persistência de triagens. */
public interface TriagemRepositoryPort {

    Triagem salvar(Triagem triagem);

    Optional<Triagem> buscarPorId(UUID id);
}
