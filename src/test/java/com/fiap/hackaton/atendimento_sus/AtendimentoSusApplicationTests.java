package com.fiap.hackaton.atendimento_sus;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * Carrega o contexto completo contra um PostgreSQL real (Testcontainers),
 * validando de quebra o casamento entre o schema Flyway e as entidades JPA
 * ({@code ddl-auto=validate}).
 */
@SpringBootTest
@Import(TestcontainersConfig.class)
class AtendimentoSusApplicationTests {

	@Test
	void contextLoads() {
	}

}
