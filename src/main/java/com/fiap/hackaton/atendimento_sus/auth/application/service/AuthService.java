package com.fiap.hackaton.atendimento_sus.auth.application.service;

import com.fiap.hackaton.atendimento_sus.auth.application.port.in.AutenticarUsuarioUseCase;
import com.fiap.hackaton.atendimento_sus.auth.application.port.in.RegistrarUsuarioUseCase;
import com.fiap.hackaton.atendimento_sus.auth.application.port.out.PasswordEncoderPort;
import com.fiap.hackaton.atendimento_sus.auth.application.port.out.TokenGeneratorPort;
import com.fiap.hackaton.atendimento_sus.auth.application.port.out.UsuarioRepositoryPort;
import com.fiap.hackaton.atendimento_sus.auth.domain.model.Email;
import com.fiap.hackaton.atendimento_sus.auth.domain.model.Usuario;
import com.fiap.hackaton.atendimento_sus.shared.exception.ConflictException;
import com.fiap.hackaton.atendimento_sus.shared.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements RegistrarUsuarioUseCase, AutenticarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenGeneratorPort tokenGenerator;

    public AuthService(UsuarioRepositoryPort usuarioRepository,
                       PasswordEncoderPort passwordEncoder,
                       TokenGeneratorPort tokenGenerator) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    @Transactional
    public Usuario registrar(RegistrarUsuarioCommand command) {
        Email email = new Email(command.email());
        if (usuarioRepository.existePorEmail(email)) {
            throw new ConflictException("Já existe usuário com o e-mail " + email.valor());
        }
        String hash = passwordEncoder.codificar(command.senha());
        Usuario usuario = Usuario.registrar(command.nome(), email, hash, command.role());
        return usuarioRepository.salvar(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Autenticacao autenticar(String email, String senha) {
        Usuario usuario = usuarioRepository.buscarPorEmail(new Email(email))
                .orElseThrow(() -> new UnauthorizedException("Credenciais inválidas"));
        if (!usuario.isAtivo() || !passwordEncoder.confere(senha, usuario.getSenhaHash())) {
            throw new UnauthorizedException("Credenciais inválidas");
        }
        String token = tokenGenerator.gerar(usuario);
        return new Autenticacao(token, usuario.getId(), usuario.getNome(), usuario.getRole());
    }
}
