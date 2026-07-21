package com.fiap.hackaton.atendimento_sus.triagem.adapter.out.ai;

import com.fiap.hackaton.atendimento_sus.triagem.application.port.out.AssistenteTriagemPort.AnaliseClinica;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.NivelRisco;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.Sintoma;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.http.HttpMethod.POST;

class OllamaAssistenteAdapterTest {

    private final OllamaProperties props = new OllamaProperties(true, "http://ollama", "llama3.2:3b", 30);

    private record Fixture(OllamaAssistenteAdapter adapter, MockRestServiceServer server) {}

    private Fixture novo() {
        RestClient.Builder builder = RestClient.builder().baseUrl(props.baseUrl());
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        return new Fixture(new OllamaAssistenteAdapter(props, builder.build()), server);
    }

    @Test
    void extraiSintomasDoJsonRetornadoPeloModelo() {
        Fixture f = novo();
        String content = "{\"sintomas\":[\"DOR_TORACICA\",\"FALTA_DE_AR\"],\"resumo\":\"dor no peito\"}";
        String body = "{\"message\":{\"role\":\"assistant\",\"content\":" + quote(content) + "}}";
        f.server().expect(requestTo("http://ollama/api/chat")).andExpect(method(POST))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));

        AnaliseClinica r = f.adapter().analisar("dor forte no peito e falta de ar");

        assertThat(r.sintomasSugeridos()).containsExactlyInAnyOrder(Sintoma.DOR_TORACICA, Sintoma.FALTA_DE_AR);
        assertThat(r.resumo()).isEqualTo("dor no peito");
        f.server().verify();
    }

    @Test
    void ignoraValoresDesconhecidosDoEnum() {
        Fixture f = novo();
        String content = "{\"sintomas\":[\"DOR_TORACICA\",\"XPTO_INVALIDO\"]}";
        String body = "{\"message\":{\"content\":" + quote(content) + "}}";
        f.server().expect(requestTo("http://ollama/api/chat"))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));

        AnaliseClinica r = f.adapter().analisar("dor no peito");

        assertThat(r.sintomasSugeridos()).containsExactly(Sintoma.DOR_TORACICA);
    }

    @Test
    void degradaParaVazioEmErroDoServidor() {
        Fixture f = novo();
        f.server().expect(requestTo("http://ollama/api/chat")).andRespond(withServerError());

        AnaliseClinica r = f.adapter().analisar("qualquer queixa");

        assertThat(r.sintomasSugeridos()).isEmpty();
        assertThat(r.resumo()).isNull();
    }

    @Test
    void naoChamaOllamaComQueixaVazia() {
        Fixture f = novo();
        // Sem expectativas no server → se chamasse, verify() falharia.
        AnaliseClinica r = f.adapter().analisar("   ");
        assertThat(r.sintomasSugeridos()).isEmpty();
        f.server().verify();
    }

    @Test
    void geraOrientacaoAPartirDoTextoDoModelo() {
        Fixture f = novo();
        String body = "{\"message\":{\"content\":\"Procure atendimento com urgência.\"}}";
        f.server().expect(requestTo("http://ollama/api/chat"))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));

        String orientacao = f.adapter().gerarOrientacao(NivelRisco.LARANJA, Set.of(Sintoma.DOR_TORACICA));

        assertThat(orientacao).isEqualTo("Procure atendimento com urgência.");
    }

    @Test
    void orientacaoDegradaParaNullEmErro() {
        Fixture f = novo();
        f.server().expect(requestTo("http://ollama/api/chat")).andRespond(withServerError());

        assertThat(f.adapter().gerarOrientacao(NivelRisco.VERMELHO, Set.of())).isNull();
    }

    /** Escapa uma string como literal JSON (com aspas). */
    private static String quote(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}
