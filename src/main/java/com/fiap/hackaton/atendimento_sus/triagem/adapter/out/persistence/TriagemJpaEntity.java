package com.fiap.hackaton.atendimento_sus.triagem.adapter.out.persistence;

import com.fiap.hackaton.atendimento_sus.triagem.domain.model.NivelRisco;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.Sintoma;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "triagem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TriagemJpaEntity {

    @Id
    private UUID id;

    @Column(name = "paciente_id", nullable = false)
    private UUID pacienteId;

    @Column(name = "profissional_id", nullable = false)
    private UUID profissionalId;

    @Column(name = "frequencia_cardiaca", nullable = false)
    private int frequenciaCardiaca;

    @Column(name = "frequencia_respiratoria", nullable = false)
    private int frequenciaRespiratoria;

    @Column(name = "pressao_sistolica", nullable = false)
    private int pressaoSistolica;

    @Column(name = "pressao_diastolica", nullable = false)
    private int pressaoDiastolica;

    @Column(nullable = false)
    private double temperatura;

    @Column(name = "saturacao_oxigenio", nullable = false)
    private int saturacaoOxigenio;

    @Column(name = "escala_dor", nullable = false)
    private int escalaDor;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_risco", nullable = false, length = 20)
    private NivelRisco nivelRisco;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;

    @Column(length = 1000)
    private String orientacao;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "triagem_sintoma", joinColumns = @JoinColumn(name = "triagem_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "sintoma", nullable = false, length = 30)
    private Set<Sintoma> sintomas = EnumSet.noneOf(Sintoma.class);
}
