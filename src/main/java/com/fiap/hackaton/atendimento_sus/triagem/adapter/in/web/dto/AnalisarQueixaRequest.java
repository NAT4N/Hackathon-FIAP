package com.fiap.hackaton.atendimento_sus.triagem.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

/** Queixa em texto livre para pré-análise assistida por IA. */
public record AnalisarQueixaRequest(@NotBlank String queixaLivre) {
}
