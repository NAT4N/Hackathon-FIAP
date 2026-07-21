package com.fiap.hackaton.atendimento_sus.agendamento.application.service;

import com.fiap.hackaton.atendimento_sus.agendamento.application.port.in.AgendarUseCase.AgendarCommand;
import com.fiap.hackaton.atendimento_sus.agendamento.application.port.out.AgendamentoRepositoryPort;
import com.fiap.hackaton.atendimento_sus.agendamento.application.port.out.ClassificacaoRiscoPort;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Agendamento;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.JanelaHorario;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Prioridade;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.TipoAtendimento;
import com.fiap.hackaton.atendimento_sus.shared.exception.ConflictException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock AgendamentoRepositoryPort agendamentoRepository;
    @Mock ClassificacaoRiscoPort classificacaoRisco;

    private final Clock clock = Clock.fixed(Instant.parse("2026-07-04T12:00:00Z"), ZoneOffset.UTC);
    private final UUID paciente = UUID.randomUUID();
    private final UUID profissional = UUID.randomUUID();

    private AgendamentoService service() {
        return new AgendamentoService(agendamentoRepository, classificacaoRisco, clock);
    }

    private LocalDateTime amanha() {
        return LocalDateTime.now(clock).plusDays(1);
    }

    private AgendarCommand comando(LocalDateTime inicio, UUID triagemId) {
        return new AgendarCommand(paciente, profissional, TipoAtendimento.CONSULTA,
                "Cardiologia", inicio, 30, triagemId, "obs");
    }

    @Test
    void agendaSemTriagemUsaPrioridadeRotina() {
        when(agendamentoRepository.listarAtivosDoProfissional(profissional)).thenReturn(List.of());
        when(agendamentoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        Agendamento ag = service().agendar(comando(amanha(), null));

        assertThat(ag.getPrioridade()).isEqualTo(Prioridade.ROTINA);
        verify(classificacaoRisco, never()).buscarPrioridade(any());
    }

    @Test
    void agendaComTriagemDerivaPrioridadeDoRisco() {
        UUID triagemId = UUID.randomUUID();
        when(classificacaoRisco.buscarPrioridade(triagemId)).thenReturn(Prioridade.MUITO_URGENTE);
        when(agendamentoRepository.listarAtivosDoProfissional(profissional)).thenReturn(List.of());
        when(agendamentoRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        Agendamento ag = service().agendar(comando(amanha(), triagemId));

        assertThat(ag.getPrioridade()).isEqualTo(Prioridade.MUITO_URGENTE);
        assertThat(ag.getTriagemId()).isEqualTo(triagemId);
    }

    @Test
    void recusaConflitoDeHorario() {
        LocalDateTime inicio = amanha();
        JanelaHorario janela = new JanelaHorario(inicio.plusMinutes(15), inicio.plusMinutes(45));
        Agendamento existente = Agendamento.agendar(UUID.randomUUID(), profissional,
                TipoAtendimento.CONSULTA, "Cardiologia", janela, null, null, Prioridade.ROTINA, clock);
        when(agendamentoRepository.listarAtivosDoProfissional(profissional)).thenReturn(List.of(existente));

        assertThatThrownBy(() -> service().agendar(comando(inicio, null)))
                .isInstanceOf(ConflictException.class);
        verify(agendamentoRepository, never()).salvar(any());
    }
}
