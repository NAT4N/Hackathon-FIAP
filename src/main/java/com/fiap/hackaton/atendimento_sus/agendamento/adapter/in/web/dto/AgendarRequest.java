package com.fiap.hackaton.atendimento_sus.agendamento.adapter.in.web.dto;

import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.TipoAtendimento;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @param triagemId opcional — quando informado, a prioridade é derivada do risco da triagem.
 */
public record AgendarRequest(
        @NotNull UUID pacienteId,
        @NotNull UUID profissionalId,
        @NotNull TipoAtendimento tipo,
        String especialidade,
        @NotNull @Future LocalDateTime inicio,
        @NotNull @Min(5) @Max(480) Integer duracaoMinutos,
        UUID triagemId,
        String observacao) {
}
