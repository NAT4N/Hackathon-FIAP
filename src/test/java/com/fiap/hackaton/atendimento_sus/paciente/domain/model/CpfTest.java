package com.fiap.hackaton.atendimento_sus.paciente.domain.model;

import com.fiap.hackaton.atendimento_sus.shared.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CpfTest {

    @Test
    void aceitaCpfValidoComOuSemMascara() {
        assertThat(new Cpf("529.982.247-25").numero()).isEqualTo("52998224725");
        assertThat(new Cpf("52998224725").numero()).isEqualTo("52998224725");
    }

    @Test
    void formataComMascara() {
        assertThat(new Cpf("52998224725").formatado()).isEqualTo("529.982.247-25");
    }

    @Test
    void rejeitaDigitoVerificadorInvalido() {
        assertThatThrownBy(() -> new Cpf("52998224724"))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void rejeitaSequenciaRepetida() {
        assertThatThrownBy(() -> new Cpf("11111111111"))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void rejeitaTamanhoIncorreto() {
        assertThatThrownBy(() -> new Cpf("123"))
                .isInstanceOf(BusinessRuleException.class);
    }
}
