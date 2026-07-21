package com.fiap.hackaton.atendimento_sus.paciente.adapter.in.web.dto;

import com.fiap.hackaton.atendimento_sus.paciente.domain.model.Paciente;
import com.fiap.hackaton.atendimento_sus.paciente.domain.model.Sexo;

import java.time.LocalDate;
import java.util.UUID;

public record PacienteResponse(
        UUID id,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        Sexo sexo,
        String telefone,
        String cartaoSus) {

    public static PacienteResponse de(Paciente p) {
        return new PacienteResponse(
                p.getId(),
                p.getNome(),
                p.getCpf().formatado(),
                p.getDataNascimento(),
                p.getSexo(),
                p.getTelefone(),
                p.getCartaoSus() == null ? null : p.getCartaoSus().numero());
    }
}
