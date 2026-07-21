package com.fiap.hackaton.atendimento_sus.paciente.application.port.in;

import com.fiap.hackaton.atendimento_sus.paciente.domain.model.Paciente;

import java.util.List;

/** Caso de uso: listar pacientes cadastrados. */
public interface ListarPacientesUseCase {

    List<Paciente> listar();
}
