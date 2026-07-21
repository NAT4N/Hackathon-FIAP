package com.fiap.hackaton.atendimento_sus.auth.adapter.in.web.dto;

import com.fiap.hackaton.atendimento_sus.auth.application.port.in.AutenticarUsuarioUseCase.Autenticacao;
import com.fiap.hackaton.atendimento_sus.auth.domain.model.Role;

import java.util.UUID;

public record TokenResponse(String token, String tipo, UUID usuarioId, String nome, Role role) {

    public static TokenResponse de(Autenticacao a) {
        return new TokenResponse(a.token(), "Bearer", a.usuarioId(), a.nome(), a.role());
    }
}
