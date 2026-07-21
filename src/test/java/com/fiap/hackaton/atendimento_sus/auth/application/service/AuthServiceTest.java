package com.fiap.hackaton.atendimento_sus.auth.application.service;

import com.fiap.hackaton.atendimento_sus.auth.application.port.in.AutenticarUsuarioUseCase.Autenticacao;
import com.fiap.hackaton.atendimento_sus.auth.application.port.in.RegistrarUsuarioUseCase.RegistrarUsuarioCommand;
import com.fiap.hackaton.atendimento_sus.auth.application.port.out.PasswordEncoderPort;
import com.fiap.hackaton.atendimento_sus.auth.application.port.out.TokenGeneratorPort;
import com.fiap.hackaton.atendimento_sus.auth.application.port.out.UsuarioRepositoryPort;
import com.fiap.hackaton.atendimento_sus.auth.domain.model.Email;
import com.fiap.hackaton.atendimento_sus.auth.domain.model.Role;
import com.fiap.hackaton.atendimento_sus.auth.domain.model.Usuario;
import com.fiap.hackaton.atendimento_sus.shared.exception.ConflictException;
import com.fiap.hackaton.atendimento_sus.shared.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock UsuarioRepositoryPort usuarioRepository;
    @Mock PasswordEncoderPort passwordEncoder;
    @Mock TokenGeneratorPort tokenGenerator;
    @InjectMocks AuthService authService;

    @Test
    void registraUsuarioNovoComSenhaCifrada() {
        when(usuarioRepository.existePorEmail(any())).thenReturn(false);
        when(passwordEncoder.codificar("segredo")).thenReturn("HASH");
        when(usuarioRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        Usuario u = authService.registrar(
                new RegistrarUsuarioCommand("Ana", "ana@sus.gov.br", "segredo", Role.PROFISSIONAL));

        assertThat(u.getSenhaHash()).isEqualTo("HASH");
        assertThat(u.getEmail().valor()).isEqualTo("ana@sus.gov.br");
        assertThat(u.getRole()).isEqualTo(Role.PROFISSIONAL);
    }

    @Test
    void recusaEmailDuplicado() {
        when(usuarioRepository.existePorEmail(any())).thenReturn(true);
        assertThatThrownBy(() -> authService.registrar(
                new RegistrarUsuarioCommand("Ana", "ana@sus.gov.br", "segredo", Role.PACIENTE)))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void autenticaComCredenciaisValidas() {
        Usuario usuario = Usuario.registrar("Ana", new Email("ana@sus.gov.br"), "HASH", Role.ADMIN);
        when(usuarioRepository.buscarPorEmail(any())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.confere("segredo", "HASH")).thenReturn(true);
        when(tokenGenerator.gerar(usuario)).thenReturn("jwt-token");

        Autenticacao auth = authService.autenticar("ana@sus.gov.br", "segredo");

        assertThat(auth.token()).isEqualTo("jwt-token");
        assertThat(auth.role()).isEqualTo(Role.ADMIN);
    }

    @Test
    void rejeitaSenhaIncorreta() {
        Usuario usuario = Usuario.registrar("Ana", new Email("ana@sus.gov.br"), "HASH", Role.ADMIN);
        when(usuarioRepository.buscarPorEmail(any())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.confere("errada", "HASH")).thenReturn(false);

        assertThatThrownBy(() -> authService.autenticar("ana@sus.gov.br", "errada"))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void rejeitaUsuarioInexistente() {
        when(usuarioRepository.buscarPorEmail(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> authService.autenticar("x@sus.gov.br", "s"))
                .isInstanceOf(UnauthorizedException.class);
    }
}
