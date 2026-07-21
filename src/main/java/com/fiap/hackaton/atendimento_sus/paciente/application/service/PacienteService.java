package com.fiap.hackaton.atendimento_sus.paciente.application.service;

import com.fiap.hackaton.atendimento_sus.paciente.application.port.in.BuscarPacienteUseCase;
import com.fiap.hackaton.atendimento_sus.paciente.application.port.in.CadastrarPacienteUseCase;
import com.fiap.hackaton.atendimento_sus.paciente.application.port.in.ListarPacientesUseCase;
import com.fiap.hackaton.atendimento_sus.paciente.application.port.out.PacienteRepositoryPort;
import com.fiap.hackaton.atendimento_sus.paciente.domain.model.CartaoSus;
import com.fiap.hackaton.atendimento_sus.paciente.domain.model.Cpf;
import com.fiap.hackaton.atendimento_sus.paciente.domain.model.Paciente;
import com.fiap.hackaton.atendimento_sus.shared.exception.ConflictException;
import com.fiap.hackaton.atendimento_sus.shared.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@Service
public class PacienteService implements CadastrarPacienteUseCase, BuscarPacienteUseCase, ListarPacientesUseCase {

    private final PacienteRepositoryPort pacienteRepository;
    private final Clock clock;

    public PacienteService(PacienteRepositoryPort pacienteRepository, Clock clock) {
        this.pacienteRepository = pacienteRepository;
        this.clock = clock;
    }

    @Override
    @Transactional
    public Paciente cadastrar(CadastrarPacienteCommand command) {
        Cpf cpf = new Cpf(command.cpf());
        if (pacienteRepository.existePorCpf(cpf)) {
            throw new ConflictException("Já existe paciente com o CPF " + cpf.formatado());
        }
        CartaoSus cartao = command.cartaoSus() == null ? null : new CartaoSus(command.cartaoSus());
        Paciente paciente = Paciente.cadastrar(command.nome(), cpf, command.dataNascimento(),
                command.sexo(), command.telefone(), cartao, clock);
        return pacienteRepository.salvar(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public Paciente buscarPorId(UUID id) {
        return pacienteRepository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Paciente não encontrado: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> listar() {
        return pacienteRepository.listar();
    }
}
