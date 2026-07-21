package com.fiap.hackaton.atendimento_sus.agendamento.application.service;

import com.fiap.hackaton.atendimento_sus.agendamento.application.port.in.AgendarUseCase;
import com.fiap.hackaton.atendimento_sus.agendamento.application.port.in.GerenciarAgendamentoUseCase;
import com.fiap.hackaton.atendimento_sus.agendamento.application.port.in.ListarAgendamentosUseCase;
import com.fiap.hackaton.atendimento_sus.agendamento.application.port.out.AgendamentoRepositoryPort;
import com.fiap.hackaton.atendimento_sus.agendamento.application.port.out.ClassificacaoRiscoPort;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Agendamento;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.JanelaHorario;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Prioridade;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.service.AgendaDomainService;
import com.fiap.hackaton.atendimento_sus.shared.exception.ConflictException;
import com.fiap.hackaton.atendimento_sus.shared.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@Service
public class AgendamentoService implements AgendarUseCase, GerenciarAgendamentoUseCase, ListarAgendamentosUseCase {

    private final AgendamentoRepositoryPort agendamentoRepository;
    private final ClassificacaoRiscoPort classificacaoRisco;
    private final AgendaDomainService agendaDomainService;
    private final Clock clock;

    public AgendamentoService(AgendamentoRepositoryPort agendamentoRepository,
                              ClassificacaoRiscoPort classificacaoRisco,
                              Clock clock) {
        this.agendamentoRepository = agendamentoRepository;
        this.classificacaoRisco = classificacaoRisco;
        this.clock = clock;
        this.agendaDomainService = new AgendaDomainService();
    }

    @Override
    @Transactional
    public Agendamento agendar(AgendarCommand command) {
        Prioridade prioridade = command.triagemId() == null
                ? Prioridade.ROTINA
                : classificacaoRisco.buscarPrioridade(command.triagemId());

        JanelaHorario janela = new JanelaHorario(
                command.inicio(), command.inicio().plusMinutes(command.duracaoMinutos()));

        Agendamento novo = Agendamento.agendar(command.pacienteId(), command.profissionalId(),
                command.tipo(), command.especialidade(), janela, command.triagemId(),
                command.observacao(), prioridade, clock);

        List<Agendamento> ativos = agendamentoRepository.listarAtivosDoProfissional(command.profissionalId());
        if (agendaDomainService.haConflito(novo, ativos)) {
            throw new ConflictException("Conflito de horário na agenda do profissional");
        }
        return agendamentoRepository.salvar(novo);
    }

    @Override
    @Transactional
    public Agendamento confirmar(UUID agendamentoId) {
        Agendamento ag = obter(agendamentoId);
        ag.confirmar();
        return agendamentoRepository.salvar(ag);
    }

    @Override
    @Transactional
    public Agendamento cancelar(UUID agendamentoId) {
        Agendamento ag = obter(agendamentoId);
        ag.cancelar();
        return agendamentoRepository.salvar(ag);
    }

    @Override
    @Transactional
    public Agendamento concluir(UUID agendamentoId) {
        Agendamento ag = obter(agendamentoId);
        ag.concluir();
        return agendamentoRepository.salvar(ag);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agendamento> listarPorProfissional(UUID profissionalId) {
        return agendamentoRepository.listarPorProfissional(profissionalId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agendamento> listarPorPaciente(UUID pacienteId) {
        return agendamentoRepository.listarPorPaciente(pacienteId);
    }

    private Agendamento obter(UUID id) {
        return agendamentoRepository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Agendamento não encontrado: " + id));
    }
}
