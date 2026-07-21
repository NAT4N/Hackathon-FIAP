package com.fiap.hackaton.atendimento_sus.auth.application.port.in;

import com.fiap.hackaton.atendimento_sus.auth.domain.model.Role;
import com.fiap.hackaton.atendimento_sus.auth.domain.model.Usuario;

/** Caso de uso: registrar um novo usuário. */
public interface RegistrarUsuarioUseCase {

    Usuario registrar(RegistrarUsuarioCommand command);

    record RegistrarUsuarioCommand(String nome, String email, String senha, Role role) {}
}
