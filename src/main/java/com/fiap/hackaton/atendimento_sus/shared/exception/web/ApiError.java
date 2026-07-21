package com.fiap.hackaton.atendimento_sus.shared.exception.web;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

/**
 * Corpo padronizado de resposta de erro da API.
 * {@code fields} só aparece no JSON quando há erros de validação de campo.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fields) {

    public static ApiError of(int status, String error, String message, String path) {
        return new ApiError(Instant.now(), status, error, message, path, null);
    }

    public static ApiError of(int status, String error, String message, String path, Map<String, String> fields) {
        return new ApiError(Instant.now(), status, error, message, path, fields);
    }
}
