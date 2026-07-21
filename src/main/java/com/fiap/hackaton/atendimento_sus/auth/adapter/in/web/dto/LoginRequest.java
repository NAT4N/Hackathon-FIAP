package com.fiap.hackaton.atendimento_sus.auth.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String email,
        @NotBlank String senha) {
}
