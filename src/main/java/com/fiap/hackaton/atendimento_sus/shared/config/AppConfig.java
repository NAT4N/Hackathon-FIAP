package com.fiap.hackaton.atendimento_sus.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Beans de infraestrutura compartilhados. O {@link Clock} é injetado nos
 * serviços de domínio/aplicação para tornar o tempo testável (ex.: proibir
 * agendamento no passado).
 */
@Configuration
public class AppConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
