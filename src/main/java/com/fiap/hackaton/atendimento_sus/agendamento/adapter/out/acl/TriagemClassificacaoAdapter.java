package com.fiap.hackaton.atendimento_sus.agendamento.adapter.out.acl;

import com.fiap.hackaton.atendimento_sus.agendamento.application.port.out.ClassificacaoRiscoPort;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Prioridade;
import com.fiap.hackaton.atendimento_sus.triagem.application.port.in.ConsultarTriagemUseCase;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.NivelRisco;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Anti-corruption layer entre agendamento e triagem. Consulta a triagem pelo
 * caso de uso público e traduz o {@link NivelRisco} (conceito da triagem) para
 * a {@link Prioridade} (conceito do agendamento), impedindo que o domínio de
 * agendamento dependa do domínio de triagem.
 */
@Component
public class TriagemClassificacaoAdapter implements ClassificacaoRiscoPort {

    private final ConsultarTriagemUseCase consultarTriagem;

    public TriagemClassificacaoAdapter(ConsultarTriagemUseCase consultarTriagem) {
        this.consultarTriagem = consultarTriagem;
    }

    @Override
    public Prioridade buscarPrioridade(UUID triagemId) {
        NivelRisco nivel = consultarTriagem.buscarPorId(triagemId).getNivelRisco();
        return traduzir(nivel);
    }

    private Prioridade traduzir(NivelRisco nivel) {
        return switch (nivel) {
            case VERMELHO -> Prioridade.EMERGENCIA;
            case LARANJA -> Prioridade.MUITO_URGENTE;
            case AMARELO -> Prioridade.URGENTE;
            case VERDE -> Prioridade.POUCO_URGENTE;
            case AZUL -> Prioridade.NAO_URGENTE;
        };
    }
}
