package com.fiap.hackaton.atendimento_sus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Exercita o fluxo completo contra um PostgreSQL real: registro/login,
 * cadastro de paciente, triagem (classificação) e agendamento com prioridade
 * derivada do risco — além de casos negativos (401/409/422). Usa ids reais
 * para respeitar as chaves estrangeiras do schema.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfig.class)
class FullFlowIntegrationTest {

    @Autowired MockMvc mockMvc;
    private final ObjectMapper json = new ObjectMapper();

    private record Sessao(String token, String usuarioId) {}

    private String jsonOf(Map<String, ?> body) throws Exception {
        return json.writeValueAsString(body);
    }

    private JsonNode tree(MvcResult r) throws Exception {
        return json.readTree(r.getResponse().getContentAsString());
    }

    private Sessao registrarELogar(String email, String role) throws Exception {
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(Map.of("nome", "Dr. Teste", "email", email, "senha", "senha123", "role", role))))
                .andExpect(status().isCreated());

        MvcResult login = mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(Map.of("email", email, "senha", "senha123"))))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode body = tree(login);
        return new Sessao(body.get("token").asText(), body.get("usuarioId").asText());
    }

    private String cadastrarPaciente(String token, String nome, String cpf) throws Exception {
        MvcResult res = mockMvc.perform(post("/api/pacientes").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(Map.of("nome", nome, "cpf", cpf,
                                "dataNascimento", "1990-05-20", "sexo", "FEMININO"))))
                .andExpect(status().isCreated())
                .andReturn();
        return tree(res).get("id").asText();
    }

    @Test
    void fluxoCompletoTriagemDefinePrioridadeDoAgendamento() throws Exception {
        Sessao prof = registrarELogar("prof1@sus.gov.br", "PROFISSIONAL");
        String pacienteId = cadastrarPaciente(prof.token(), "Maria", "52998224725");

        // Triagem com dor torácica → LARANJA
        MvcResult triRes = mockMvc.perform(post("/api/triagens").header("Authorization", "Bearer " + prof.token())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(Map.of(
                                "pacienteId", pacienteId,
                                "frequenciaCardiaca", 80, "frequenciaRespiratoria", 16,
                                "pressaoSistolica", 120, "pressaoDiastolica", 80,
                                "temperatura", 36.5, "saturacaoOxigenio", 98, "escalaDor", 3,
                                "sintomas", List.of("DOR_TORACICA")))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nivelRisco").value("LARANJA"))
                .andReturn();
        String triagemId = tree(triRes).get("id").asText();

        // Agenda referenciando a triagem → prioridade MUITO_URGENTE
        LocalDateTime inicio = LocalDateTime.now().plusDays(1).withNano(0);
        mockMvc.perform(post("/api/agendamentos").header("Authorization", "Bearer " + prof.token())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(Map.of(
                                "pacienteId", pacienteId, "profissionalId", prof.usuarioId(),
                                "tipo", "CONSULTA", "especialidade", "Cardiologia",
                                "inicio", inicio.toString(), "duracaoMinutos", 30,
                                "triagemId", triagemId))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.prioridade").value("MUITO_URGENTE"))
                .andExpect(jsonPath("$.status").value("AGENDADO"));
    }

    @Test
    void rotaProtegidaSemTokenRetorna401() throws Exception {
        mockMvc.perform(get("/api/pacientes")).andExpect(status().isUnauthorized());
    }

    @Test
    void conflitoDeHorarioRetorna409() throws Exception {
        Sessao prof = registrarELogar("prof2@sus.gov.br", "PROFISSIONAL");
        String pacienteId = cadastrarPaciente(prof.token(), "João", "11144477735");
        LocalDateTime inicio = LocalDateTime.now().plusDays(2).withNano(0);

        mockMvc.perform(post("/api/agendamentos").header("Authorization", "Bearer " + prof.token())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(Map.of("pacienteId", pacienteId, "profissionalId", prof.usuarioId(),
                                "tipo", "CONSULTA", "inicio", inicio.toString(), "duracaoMinutos", 30))))
                .andExpect(status().isCreated());

        // Mesmo profissional, horário sobreposto → 409
        mockMvc.perform(post("/api/agendamentos").header("Authorization", "Bearer " + prof.token())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(Map.of("pacienteId", pacienteId, "profissionalId", prof.usuarioId(),
                                "tipo", "CONSULTA", "inicio", inicio.plusMinutes(15).toString(),
                                "duracaoMinutos", 30))))
                .andExpect(status().isConflict());
    }

    @Test
    void concluirAgendamentoNaoConfirmadoRetorna422() throws Exception {
        Sessao prof = registrarELogar("prof3@sus.gov.br", "PROFISSIONAL");
        String pacienteId = cadastrarPaciente(prof.token(), "Ana", "12345678909");
        LocalDateTime inicio = LocalDateTime.now().plusDays(3).withNano(0);

        MvcResult ag = mockMvc.perform(post("/api/agendamentos").header("Authorization", "Bearer " + prof.token())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(Map.of("pacienteId", pacienteId, "profissionalId", prof.usuarioId(),
                                "tipo", "EXAME", "inicio", inicio.toString(), "duracaoMinutos", 30))))
                .andExpect(status().isCreated())
                .andReturn();
        String agId = tree(ag).get("id").asText();

        // AGENDADO → concluir (REALIZADO) não é transição válida → 422
        mockMvc.perform(post("/api/agendamentos/" + agId + "/concluir").header("Authorization", "Bearer " + prof.token()))
                .andExpect(status().isUnprocessableEntity());
    }
}
