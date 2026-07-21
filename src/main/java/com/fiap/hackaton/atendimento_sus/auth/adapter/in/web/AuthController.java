package com.fiap.hackaton.atendimento_sus.auth.adapter.in.web;

import com.fiap.hackaton.atendimento_sus.auth.adapter.in.web.dto.LoginRequest;
import com.fiap.hackaton.atendimento_sus.auth.adapter.in.web.dto.RegistrarUsuarioRequest;
import com.fiap.hackaton.atendimento_sus.auth.adapter.in.web.dto.TokenResponse;
import com.fiap.hackaton.atendimento_sus.auth.adapter.in.web.dto.UsuarioResponse;
import com.fiap.hackaton.atendimento_sus.auth.application.port.in.AutenticarUsuarioUseCase;
import com.fiap.hackaton.atendimento_sus.auth.application.port.in.RegistrarUsuarioUseCase;
import com.fiap.hackaton.atendimento_sus.auth.application.port.in.RegistrarUsuarioUseCase.RegistrarUsuarioCommand;
import com.fiap.hackaton.atendimento_sus.auth.domain.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Registro e login de usuários")
public class AuthController {

    private final RegistrarUsuarioUseCase registrarUsuario;
    private final AutenticarUsuarioUseCase autenticarUsuario;

    public AuthController(RegistrarUsuarioUseCase registrarUsuario, AutenticarUsuarioUseCase autenticarUsuario) {
        this.registrarUsuario = registrarUsuario;
        this.autenticarUsuario = autenticarUsuario;
    }

    @PostMapping("/register")
    @Operation(summary = "Registra um novo usuário")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegistrarUsuarioRequest req) {
        Usuario usuario = registrarUsuario.registrar(
                new RegistrarUsuarioCommand(req.nome(), req.email(), req.senha(), req.role()));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.de(usuario));
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica e retorna um token JWT")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest req) {
        var autenticacao = autenticarUsuario.autenticar(req.email(), req.senha());
        return ResponseEntity.ok(TokenResponse.de(autenticacao));
    }
}
