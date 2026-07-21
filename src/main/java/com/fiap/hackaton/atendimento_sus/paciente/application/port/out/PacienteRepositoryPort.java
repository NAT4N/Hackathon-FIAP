package com.fiap.hackaton.atendimento_sus.paciente.application.port.out;

import com.fiap.hackaton.atendimento_sus.paciente.domain.model.Cpf;
import com.fiap.hackaton.atendimento_sus.paciente.domain.model.Paciente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Porta de saída para persistência de pacientes. */
public interface PacienteRepositoryPort {

    Paciente salvar(Paciente paciente);

    Optional<Paciente> buscarPorId(UUID id);

    List<Paciente> listar();

    boolean existePorCpf(Cpf cpf);
}
