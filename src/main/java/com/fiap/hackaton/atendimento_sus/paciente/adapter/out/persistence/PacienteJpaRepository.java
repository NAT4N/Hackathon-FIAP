package com.fiap.hackaton.atendimento_sus.paciente.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PacienteJpaRepository extends JpaRepository<PacienteJpaEntity, UUID> {

    boolean existsByCpf(String cpf);
}
