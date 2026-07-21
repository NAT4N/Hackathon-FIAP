package com.fiap.hackaton.atendimento_sus.paciente.adapter.out.persistence;

import com.fiap.hackaton.atendimento_sus.paciente.domain.model.CartaoSus;
import com.fiap.hackaton.atendimento_sus.paciente.domain.model.Cpf;
import com.fiap.hackaton.atendimento_sus.paciente.domain.model.Paciente;

final class PacientePersistenceMapper {

    private PacientePersistenceMapper() {
    }

    static PacienteJpaEntity toEntity(Paciente p) {
        return new PacienteJpaEntity(
                p.getId(),
                p.getNome(),
                p.getCpf().numero(),
                p.getDataNascimento(),
                p.getSexo(),
                p.getTelefone(),
                p.getCartaoSus() == null ? null : p.getCartaoSus().numero());
    }

    static Paciente toDomain(PacienteJpaEntity e) {
        return Paciente.reconstituir(
                e.getId(),
                e.getNome(),
                new Cpf(e.getCpf()),
                e.getDataNascimento(),
                e.getSexo(),
                e.getTelefone(),
                e.getCartaoSus() == null ? null : new CartaoSus(e.getCartaoSus()));
    }
}
