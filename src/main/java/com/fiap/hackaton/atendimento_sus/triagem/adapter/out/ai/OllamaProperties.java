package com.fiap.hackaton.atendimento_sus.triagem.adapter.out.ai;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuração da integração com o Ollama.
 *
 * @param enabled        liga o adapter real (senão usa o no-op)
 * @param baseUrl        URL base do servidor Ollama
 * @param model          modelo/imagem a usar (ex.: llama3.2:3b)
 * @param timeoutSeconds timeout das chamadas HTTP
 */
@ConfigurationProperties(prefix = "app.ai.ollama")
public record OllamaProperties(
        boolean enabled,
        String baseUrl,
        String model,
        int timeoutSeconds) {

    public OllamaProperties {
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "http://localhost:11434";
        }
        if (model == null || model.isBlank()) {
            model = "llama3.2:3b";
        }
        if (timeoutSeconds <= 0) {
            timeoutSeconds = 60;
        }
    }
}
