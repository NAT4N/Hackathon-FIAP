package com.fiap.hackaton.atendimento_sus.agendamento.domain.model;

import com.fiap.hackaton.atendimento_sus.shared.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AgendamentoTest {

    private static final ZoneId ZONA = ZoneOffset.UTC;
    // "Agora" fixo: 2026-07-04T12:00Z
    private final Clock relogio = Clock.fixed(Instant.parse("2026-07-04T12:00:00Z"), ZONA);

    private JanelaHorario janelaFutura() {
        LocalDateTime inicio = LocalDateTime.now(relogio).plusDays(1);
        return new JanelaHorario(inicio, inicio.plusMinutes(30));
    }

    private Agendamento novoAgendamento(JanelaHorario janela) {
        return Agendamento.agendar(UUID.randomUUID(), UUID.randomUUID(), TipoAtendimento.CONSULTA,
                "Cardiologia", janela, null, null, Prioridade.ROTINA, relogio);
    }

    @Test
    void agendaNoFuturoComStatusAgendado() {
        Agendamento ag = novoAgendamento(janelaFutura());
        assertThat(ag.getStatus()).isEqualTo(StatusAgendamento.AGENDADO);
        assertThat(ag.getPrioridade()).isEqualTo(Prioridade.ROTINA);
    }

    @Test
    void recusaAgendamentoNoPassado() {
        LocalDateTime inicioPassado = LocalDateTime.now(relogio).minusHours(1);
        JanelaHorario janela = new JanelaHorario(inicioPassado, inicioPassado.plusMinutes(30));
        assertThatThrownBy(() -> novoAgendamento(janela))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("passado");
    }

    @Test
    void fluxoConfirmarConcluir() {
        Agendamento ag = novoAgendamento(janelaFutura());
        ag.confirmar();
        assertThat(ag.getStatus()).isEqualTo(StatusAgendamento.CONFIRMADO);
        ag.concluir();
        assertThat(ag.getStatus()).isEqualTo(StatusAgendamento.REALIZADO);
    }

    @Test
    void naoPermiteConcluirSemConfirmar() {
        Agendamento ag = novoAgendamento(janelaFutura());
        assertThatThrownBy(ag::concluir)
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Transição");
    }

    @Test
    void naoPermiteCancelarAgendamentoJaRealizado() {
        Agendamento ag = novoAgendamento(janelaFutura());
        ag.confirmar();
        ag.concluir();
        assertThatThrownBy(ag::cancelar).isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void prioridadeNulaViraRotina() {
        Agendamento ag = Agendamento.agendar(UUID.randomUUID(), UUID.randomUUID(),
                TipoAtendimento.EXAME, "Raio-X", janelaFutura(), null, null, null, relogio);
        assertThat(ag.getPrioridade()).isEqualTo(Prioridade.ROTINA);
    }
}
