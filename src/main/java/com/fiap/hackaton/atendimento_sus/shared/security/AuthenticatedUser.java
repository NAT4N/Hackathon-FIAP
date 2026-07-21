package com.fiap.hackaton.atendimento_sus.shared.security;

import java.util.UUID;

/**
 * Principal autenticado extraído do JWT. Fica disponível no
 * {@code SecurityContext} e pode ser injetado nos controllers via
 * {@code @AuthenticationPrincipal}.
 */
public record AuthenticatedUser(UUID id, String email, String role) {
}
