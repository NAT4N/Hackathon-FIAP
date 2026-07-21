package com.fiap.hackaton.atendimento_sus.paciente.adapter.in.web;

import com.fiap.hackaton.atendimento_sus.paciente.adapter.in.web.dto.CadastrarPacienteRequest;
import com.fiap.hackaton.atendimento_sus.paciente.adapter.in.web.dto.PacienteResponse;
import com.fiap.hackaton.atendimento_sus.paciente.application.port.in.BuscarPacienteUseCase;
import com.fiap.hackaton.atendimento_sus.paciente.application.port.in.CadastrarPacienteUseCase;
import com.fiap.hackaton.atendimento_sus.paciente.application.port.in.CadastrarPacienteUseCase.CadastrarPacienteCommand;
import com.fiap.hackaton.atendimento_sus.paciente.application.port.in.ListarPacientesUseCase;
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
@RequestMapping("/api/pacientes")
@Tag(name = "Pacientes", description = "Cadastro e consulta de pacientes")
public class PacienteController {

    private final CadastrarPacienteUseCase cadastrarPaciente;
    private final BuscarPacienteUseCase buscarPaciente;
    private final ListarPacientesUseCase listarPacientes;

    public PacienteController(CadastrarPacienteUseCase cadastrarPaciente,
                              BuscarPacienteUseCase buscarPaciente,
                              ListarPacientesUseCase listarPacientes) {
        this.cadastrarPaciente = cadastrarPaciente;
        this.buscarPaciente = buscarPaciente;
        this.listarPacientes = listarPacientes;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Cadastra um novo paciente")
    public ResponseEntity<PacienteResponse> cadastrar(@Valid @RequestBody CadastrarPacienteRequest req) {
        var paciente = cadastrarPaciente.cadastrar(new CadastrarPacienteCommand(
                req.nome(), req.cpf(), req.dataNascimento(), req.sexo(), req.telefone(), req.cartaoSus()));
        return ResponseEntity.status(HttpStatus.CREATED).body(PacienteResponse.de(paciente));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um paciente por id")
    public ResponseEntity<PacienteResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(PacienteResponse.de(buscarPaciente.buscarPorId(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Lista todos os pacientes")
    public ResponseEntity<List<PacienteResponse>> listar() {
        return ResponseEntity.ok(listarPacientes.listar().stream().map(PacienteResponse::de).toList());
    }
}
