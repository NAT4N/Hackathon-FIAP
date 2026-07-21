package com.fiap.hackaton.atendimento_sus.auth.application.port.out;

/** Porta de saída para hashing/verificação de senhas. */
public interface PasswordEncoderPort {

    String codificar(String senhaPura);

    boolean confere(String senhaPura, String hash);
}
