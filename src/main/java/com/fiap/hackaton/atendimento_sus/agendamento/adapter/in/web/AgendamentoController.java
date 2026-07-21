package com.fiap.hackaton.atendimento_sus.agendamento.adapter.in.web;

import com.fiap.hackaton.atendimento_sus.agendamento.adapter.in.web.dto.AgendamentoResponse;
import com.fiap.hackaton.atendimento_sus.agendamento.adapter.in.web.dto.AgendarRequest;
import com.fiap.hackaton.atendimento_sus.agendamento.application.port.in.AgendarUseCase;
import com.fiap.hackaton.atendimento_sus.agendamento.application.port.in.AgendarUseCase.AgendarCommand;
import com.fiap.hackaton.atendimento_sus.agendamento.application.port.in.GerenciarAgendamentoUseCase;
import com.fiap.hackaton.atendimento_sus.agendamento.application.port.in.ListarAgendamentosUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/agendamentos")
@Tag(name = "Agendamentos", description = "Agendamento de consultas e exames")
public class AgendamentoController {

    private final AgendarUseCase agendar;
    private final GerenciarAgendamentoUseCase gerenciar;
    private final ListarAgendamentosUseCase listar;

    public AgendamentoController(AgendarUseCase agendar, GerenciarAgendamentoUseCase gerenciar,
                                 ListarAgendamentosUseCase listar) {
        this.agendar = agendar;
        this.gerenciar = gerenciar;
        this.listar = listar;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PACIENTE', 'PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Agenda consulta/exame; prioridade derivada da triagem, se informada")
    public ResponseEntity<AgendamentoResponse> agendar(@Valid @RequestBody AgendarRequest req) {
        var ag = agendar.agendar(new AgendarCommand(
                req.pacienteId(), req.profissionalId(), req.tipo(), req.especialidade(),
                req.inicio(), req.duracaoMinutos(), req.triagemId(), req.observacao()));
        return ResponseEntity.status(HttpStatus.CREATED).body(AgendamentoResponse.de(ag));
    }

    @PostMapping("/{id}/confirmar")
    @PreAuthorize("hasAnyRole('PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Confirma um agendamento")
    public ResponseEntity<AgendamentoResponse> confirmar(@PathVariable UUID id) {
        return ResponseEntity.ok(AgendamentoResponse.de(gerenciar.confirmar(id)));
    }

    @PostMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('PACIENTE', 'PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Cancela um agendamento")
    public ResponseEntity<AgendamentoResponse> cancelar(@PathVariable UUID id) {
        return ResponseEntity.ok(AgendamentoResponse.de(gerenciar.cancelar(id)));
    }

    @PostMapping("/{id}/concluir")
    @PreAuthorize("hasAnyRole('PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Conclui (realiza) um agendamento confirmado")
    public ResponseEntity<AgendamentoResponse> concluir(@PathVariable UUID id) {
        return ResponseEntity.ok(AgendamentoResponse.de(gerenciar.concluir(id)));
    }

    @GetMapping("/profissional/{profissionalId}")
    @PreAuthorize("hasAnyRole('PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Lista agendamentos de um profissional")
    public ResponseEntity<List<AgendamentoResponse>> listarPorProfissional(@PathVariable UUID profissionalId) {
        return ResponseEntity.ok(listar.listarPorProfissional(profissionalId).stream()
                .map(AgendamentoResponse::de).toList());
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Lista agendamentos de um paciente")
    public ResponseEntity<List<AgendamentoResponse>> listarPorPaciente(@PathVariable UUID pacienteId) {
        return ResponseEntity.ok(listar.listarPorPaciente(pacienteId).stream()
                .map(AgendamentoResponse::de).toList());
    }
}
