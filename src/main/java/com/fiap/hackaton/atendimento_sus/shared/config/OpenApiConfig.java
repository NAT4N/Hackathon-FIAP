package com.fiap.hackaton.atendimento_sus.shared.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Documentação OpenAPI/Swagger com esquema de autenticação JWT (Bearer)
 * registrado globalmente, para que o botão "Authorize" apareça no Swagger UI.
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME = "bearer-jwt";

    @Bean
    public OpenAPI atendimentoSusOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Atendimento SUS — API")
                        .description("MVP de agendamento de consultas/exames com triagem inteligente (Manchester). "
                                + "Arquitetura hexagonal. Hackathon FIAP.")
                        .version("v1")
                        .license(new License().name("MIT")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
