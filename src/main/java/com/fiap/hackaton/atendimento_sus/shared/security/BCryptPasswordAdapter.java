package com.fiap.hackaton.atendimento_sus.shared.security;

import com.fiap.hackaton.atendimento_sus.auth.application.port.out.PasswordEncoderPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/** Implementa o hashing de senhas com BCrypt (Spring Security). */
@Component
public class BCryptPasswordAdapter implements PasswordEncoderPort {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String codificar(String senhaPura) {
        return encoder.encode(senhaPura);
    }

    @Override
    public boolean confere(String senhaPura, String hash) {
        return encoder.matches(senhaPura, hash);
    }
}
