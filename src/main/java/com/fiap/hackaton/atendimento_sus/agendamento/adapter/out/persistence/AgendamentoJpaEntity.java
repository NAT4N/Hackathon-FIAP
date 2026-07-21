package com.fiap.hackaton.atendimento_sus.agendamento.adapter.out.persistence;

import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Prioridade;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.StatusAgendamento;
import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.TipoAtendimento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "agendamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoJpaEntity {

    @Id
    private UUID id;

    @Column(name = "paciente_id", nullable = false)
    private UUID pacienteId;

    @Column(name = "profissional_id", nullable = false)
    private UUID profissionalId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoAtendimento tipo;

    @Column(length = 120)
    private String especialidade;

    @Column(nullable = false)
    private LocalDateTime inicio;

    @Column(nullable = false)
    private LocalDateTime fim;

    @Column(name = "triagem_id")
    private UUID triagemId;

    @Column(length = 500)
    private String observacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAgendamento status;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;
}
