package com.fiap.hackaton.atendimento_sus.triagem.domain.model;

import com.fiap.hackaton.atendimento_sus.shared.exception.BusinessRuleException;
import lombok.Getter;

import java.time.Clock;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

/**
 * Registro de uma triagem realizada por um profissional para um paciente.
 * O nível de risco é resultado da classificação (calculado fora e informado
 * na criação) e é imutável após registrado.
 */
@Getter
public class Triagem {

    private final UUID id;
    private final UUID pacienteId;
    private final UUID profissionalId;
    private final SinaisVitais sinaisVitais;
    private final Set<Sintoma> sintomas;
    private final NivelRisco nivelRisco;
    private final Instant criadoEm;
    /** Orientação ao paciente gerada por IA (best-effort); pode ser nula. */
    private String orientacao;

    private Triagem(UUID id, UUID pacienteId, UUID profissionalId, SinaisVitais sinaisVitais,
                    Set<Sintoma> sintomas, NivelRisco nivelRisco, Instant criadoEm, String orientacao) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.profissionalId = profissionalId;
        this.sinaisVitais = sinaisVitais;
        this.sintomas = Collections.unmodifiableSet(sintomas);
        this.nivelRisco = nivelRisco;
        this.criadoEm = criadoEm;
        this.orientacao = orientacao;
    }

    /** Anexa a orientação (opcional) gerada após a classificação. */
    public void atribuirOrientacao(String orientacao) {
        this.orientacao = orientacao;
    }

    public static Triagem registrar(UUID pacienteId, UUID profissionalId, SinaisVitais sinaisVitais,
                                    Set<Sintoma> sintomas, NivelRisco nivelRisco, Clock clock) {
        if (pacienteId == null) {
            throw new BusinessRuleException("Paciente é obrigatório na triagem");
        }
        if (profissionalId == null) {
            throw new BusinessRuleException("Profissional é obrigatório na triagem");
        }
        if (sinaisVitais == null) {
            throw new BusinessRuleException("Sinais vitais são obrigatórios na triagem");
        }
        if (nivelRisco == null) {
            throw new BusinessRuleException("Nível de risco é obrigatório na triagem");
        }
        Set<Sintoma> copia = (sintomas == null || sintomas.isEmpty())
                ? EnumSet.noneOf(Sintoma.class) : EnumSet.copyOf(sintomas);
        return new Triagem(UUID.randomUUID(), pacienteId, profissionalId, sinaisVitais,
                copia, nivelRisco, Instant.now(clock), null);
    }

    public static Triagem reconstituir(UUID id, UUID pacienteId, UUID profissionalId, SinaisVitais sinaisVitais,
                                       Set<Sintoma> sintomas, NivelRisco nivelRisco, Instant criadoEm,
                                       String orientacao) {
        Set<Sintoma> copia = (sintomas == null || sintomas.isEmpty())
                ? EnumSet.noneOf(Sintoma.class) : EnumSet.copyOf(sintomas);
        return new Triagem(id, pacienteId, profissionalId, sinaisVitais, copia, nivelRisco, criadoEm, orientacao);
    }
}
