package com.fiap.hackaton.atendimento_sus.agendamento.domain.model;

import com.fiap.hackaton.atendimento_sus.shared.exception.BusinessRuleException;
import lombok.Getter;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Agendamento de consulta ou exame. Encapsula o ciclo de vida (status) e as
 * regras de transição, além de proibir agendamento no passado.
 */
@Getter
public class Agendamento {

    private final UUID id;
    private final UUID pacienteId;
    private final UUID profissionalId;
    private final TipoAtendimento tipo;
    private final String especialidade;
    private final JanelaHorario janela;
    private final UUID triagemId;   // opcional (pode ser null)
    private final String observacao;
    private final LocalDateTime criadoEm;
    private Prioridade prioridade;
    private StatusAgendamento status;

    private Agendamento(UUID id, UUID pacienteId, UUID profissionalId, TipoAtendimento tipo,
                        String especialidade, JanelaHorario janela, UUID triagemId, String observacao,
                        Prioridade prioridade, StatusAgendamento status, LocalDateTime criadoEm) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.profissionalId = profissionalId;
        this.tipo = tipo;
        this.especialidade = especialidade;
        this.janela = janela;
        this.triagemId = triagemId;
        this.observacao = observacao;
        this.prioridade = prioridade;
        this.status = status;
        this.criadoEm = criadoEm;
    }

    /** Cria um novo agendamento no status AGENDADO, recusando início no passado. */
    public static Agendamento agendar(UUID pacienteId, UUID profissionalId, TipoAtendimento tipo,
                                      String especialidade, JanelaHorario janela, UUID triagemId,
                                      String observacao, Prioridade prioridade, Clock clock) {
        if (pacienteId == null) {
            throw new BusinessRuleException("Paciente é obrigatório no agendamento");
        }
        if (profissionalId == null) {
            throw new BusinessRuleException("Profissional é obrigatório no agendamento");
        }
        if (tipo == null) {
            throw new BusinessRuleException("Tipo de atendimento é obrigatório");
        }
        if (janela == null) {
            throw new BusinessRuleException("Janela de horário é obrigatória");
        }
        if (janela.inicio().isBefore(LocalDateTime.now(clock))) {
            throw new BusinessRuleException("Não é possível agendar em um horário no passado");
        }
        Prioridade efetiva = prioridade == null ? Prioridade.ROTINA : prioridade;
        return new Agendamento(UUID.randomUUID(), pacienteId, profissionalId, tipo, especialidade,
                janela, triagemId, observacao, efetiva, StatusAgendamento.AGENDADO, LocalDateTime.now(clock));
    }

    public static Agendamento reconstituir(UUID id, UUID pacienteId, UUID profissionalId, TipoAtendimento tipo,
                                           String especialidade, JanelaHorario janela, UUID triagemId,
                                           String observacao, Prioridade prioridade, StatusAgendamento status,
                                           LocalDateTime criadoEm) {
        return new Agendamento(id, pacienteId, profissionalId, tipo, especialidade, janela, triagemId,
                observacao, prioridade, status, criadoEm);
    }

    public void confirmar() {
        transicionar(StatusAgendamento.CONFIRMADO);
    }

    public void cancelar() {
        transicionar(StatusAgendamento.CANCELADO);
    }

    public void concluir() {
        transicionar(StatusAgendamento.REALIZADO);
    }

    public void marcarNaoComparecimento() {
        transicionar(StatusAgendamento.NAO_COMPARECEU);
    }

    private void transicionar(StatusAgendamento destino) {
        if (!status.podeTransicionarPara(destino)) {
            throw new BusinessRuleException(
                    "Transição de status inválida: %s → %s".formatted(status, destino));
        }
        this.status = destino;
    }
}
