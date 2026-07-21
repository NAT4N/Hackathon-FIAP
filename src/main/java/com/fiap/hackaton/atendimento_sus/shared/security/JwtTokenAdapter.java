package com.fiap.hackaton.atendimento_sus.shared.security;

import com.fiap.hackaton.atendimento_sus.auth.application.port.out.TokenGeneratorPort;
import com.fiap.hackaton.atendimento_sus.auth.domain.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Adapter JWT (jjwt / HS256) que implementa a geração de tokens (porta do
 * contexto auth) e a validação de tokens (porta de segurança do filtro).
 * Claims: {@code sub} (id), {@code email}, {@code role}, {@code exp}.
 */
@Component
public class JwtTokenAdapter implements TokenGeneratorPort, TokenValidatorPort {

    private final SecretKey chave;
    private final long expiracaoMinutos;
    private final Clock clock;

    public JwtTokenAdapter(@Value("${app.security.jwt.secret}") String secret,
                           @Value("${app.security.jwt.expiration-minutes}") long expiracaoMinutos,
                           Clock clock) {
        this.chave = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiracaoMinutos = expiracaoMinutos;
        this.clock = clock;
    }

    @Override
    public String gerar(Usuario usuario) {
        Instant agora = Instant.now(clock);
        Instant expiracao = agora.plus(expiracaoMinutos, ChronoUnit.MINUTES);
        return Jwts.builder()
                .subject(usuario.getId().toString())
                .claim("email", usuario.getEmail().valor())
                .claim("role", usuario.getRole().name())
                .issuedAt(Date.from(agora))
                .expiration(Date.from(expiracao))
                .signWith(chave)
                .compact();
    }

    @Override
    public Optional<AuthenticatedUser> validar(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(chave)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            UUID id = UUID.fromString(claims.getSubject());
            String email = claims.get("email", String.class);
            String role = claims.get("role", String.class);
            return Optional.of(new AuthenticatedUser(id, email, role));
        } catch (JwtException | IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
}
