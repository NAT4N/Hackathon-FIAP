package com.fiap.hackaton.atendimento_sus.triagem.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TriagemJpaRepository extends JpaRepository<TriagemJpaEntity, UUID> {
}
