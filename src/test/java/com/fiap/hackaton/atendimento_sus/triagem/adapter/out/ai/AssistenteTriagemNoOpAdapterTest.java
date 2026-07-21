package com.fiap.hackaton.atendimento_sus.triagem.adapter.out.ai;

import com.fiap.hackaton.atendimento_sus.triagem.application.port.out.AssistenteTriagemPort.AnaliseClinica;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.NivelRisco;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AssistenteTriagemNoOpAdapterTest {

    private final AssistenteTriagemNoOpAdapter adapter = new AssistenteTriagemNoOpAdapter();

    @Test
    void analisarRetornaVazio() {
        AnaliseClinica r = adapter.analisar("dor no peito");
        assertThat(r.sintomasSugeridos()).isEmpty();
        assertThat(r.resumo()).isNull();
    }

    @Test
    void orientacaoRetornaNull() {
        assertThat(adapter.gerarOrientacao(NivelRisco.LARANJA, Set.of())).isNull();
    }
}
