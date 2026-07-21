package com.fiap.hackaton.atendimento_sus.auth.application.port.out;

import com.fiap.hackaton.atendimento_sus.auth.domain.model.Usuario;

/** Porta de saída para geração de tokens de acesso (JWT). */
public interface TokenGeneratorPort {

    String gerar(Usuario usuario);
}
