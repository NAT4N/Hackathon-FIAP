package com.fiap.hackaton.atendimento_sus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AtendimentoSusApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtendimentoSusApplication.class, args);
	}

}
