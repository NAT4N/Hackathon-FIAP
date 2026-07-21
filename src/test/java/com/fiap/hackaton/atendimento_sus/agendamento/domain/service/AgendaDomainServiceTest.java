package com.fiap.hackaton.atendimento_sus.agendamento.domain.service;

import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Agendamento;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.JanelaHorario;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Prioridade;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.TipoAtendimento;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AgendaDomainServiceTest {

    private final AgendaDomainService service = new AgendaDomainService();
    private final Clock relogio = Clock.fixed(Instant.parse("2026-07-04T12:00:00Z"), ZoneOffset.UTC);
    private final UUID profissional = UUID.randomUUID();
    private final LocalDateTime base = LocalDateTime.now(relogio).plusDays(1);

    private Agendamento agendamento(UUID prof, LocalDateTime inicio, int duracaoMin) {
        JanelaHorario janela = new JanelaHorario(inicio, inicio.plusMinutes(duracaoMin));
        return Agendamento.agendar(UUID.randomUUID(), prof, TipoAtendimento.CONSULTA,
                "Clínica", janela, null, null, Prioridade.ROTINA, relogio);
    }

    @Test
    void detectaSobreposicaoDoMesmoProfissional() {
        Agendamento existente = agendamento(profissional, base, 30);
        Agendamento novo = agendamento(profissional, base.plusMinutes(15), 30);
        assertThat(service.haConflito(novo, List.of(existente))).isTrue();
    }

    @Test
    void naoConflitaHorariosAdjacentes() {
        Agendamento existente = agendamento(profissional, base, 30);
        Agendamento novo = agendamento(profissional, base.plusMinutes(30), 30);
        assertThat(service.haConflito(novo, List.of(existente))).isFalse();
    }

    @Test
    void naoConflitaProfissionaisDiferentes() {
        Agendamento existente = agendamento(profissional, base, 30);
        Agendamento novo = agendamento(UUID.randomUUID(), base.plusMinutes(15), 30);
        assertThat(service.haConflito(novo, List.of(existente))).isFalse();
    }

    @Test
    void ignoraAgendamentosCancelados() {
        Agendamento cancelado = agendamento(profissional, base, 30);
        cancelado.cancelar();
        Agendamento novo = agendamento(profissional, base.plusMinutes(15), 30);
        assertThat(service.haConflito(novo, List.of(cancelado))).isFalse();
    }
}
