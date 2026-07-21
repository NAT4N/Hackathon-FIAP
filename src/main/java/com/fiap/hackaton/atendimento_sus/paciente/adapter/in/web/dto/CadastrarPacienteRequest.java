package com.fiap.hackaton.atendimento_sus.paciente.adapter.in.web.dto;

import com.fiap.hackaton.atendimento_sus.paciente.domain.model.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record CadastrarPacienteRequest(
        @NotBlank String nome,
        @NotBlank String cpf,
        @NotNull @Past LocalDate dataNascimento,
        @NotNull Sexo sexo,
        String telefone,
        String cartaoSus) {
}
