package com.fiap.hackaton.atendimento_sus.auth.adapter.in.web.dto;

import com.fiap.hackaton.atendimento_sus.auth.domain.model.Role;
import com.fiap.hackaton.atendimento_sus.auth.domain.model.Usuario;

import java.util.UUID;

public record UsuarioResponse(UUID id, String nome, String email, Role role) {

    public static UsuarioResponse de(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail().valor(), u.getRole());
    }
}
