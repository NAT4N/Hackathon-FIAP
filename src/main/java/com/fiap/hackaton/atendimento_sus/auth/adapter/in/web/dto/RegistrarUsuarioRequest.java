package com.fiap.hackaton.atendimento_sus.auth.adapter.in.web.dto;

import com.fiap.hackaton.atendimento_sus.auth.domain.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrarUsuarioRequest(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, max = 100) String senha,
        @NotNull Role role) {
}
