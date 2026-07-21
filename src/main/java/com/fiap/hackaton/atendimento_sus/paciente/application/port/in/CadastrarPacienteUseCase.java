package com.fiap.hackaton.atendimento_sus.paciente.application.port.in;

import com.fiap.hackaton.atendimento_sus.paciente.domain.model.Paciente;
import com.fiap.hackaton.atendimento_sus.paciente.domain.model.Sexo;

import java.time.LocalDate;

/** Caso de uso: cadastrar um novo paciente. */
public interface CadastrarPacienteUseCase {

    Paciente cadastrar(CadastrarPacienteCommand command);

    record CadastrarPacienteCommand(
            String nome,
            String cpf,
            LocalDate dataNascimento,
            Sexo sexo,
            String telefone,
            String cartaoSus) {}
}
