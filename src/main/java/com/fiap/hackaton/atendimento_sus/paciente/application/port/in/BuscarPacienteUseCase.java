package com.fiap.hackaton.atendimento_sus.paciente.application.port.in;

import com.fiap.hackaton.atendimento_sus.paciente.domain.model.Paciente;

import java.util.UUID;

/** Caso de uso: buscar um paciente por id. */
public interface BuscarPacienteUseCase {

    Paciente buscarPorId(UUID id);
}
