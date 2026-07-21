package com.fiap.hackaton.atendimento_sus.auth.domain.model;

import com.fiap.hackaton.atendimento_sus.shared.exception.BusinessRuleException;
import lombok.Getter;

import java.util.UUID;

/**
 * Usuário do sistema. A senha trafega e é persistida apenas como hash — o
 * hashing é responsabilidade de uma porta de saída (nunca do domínio).
 */
@Getter
public class Usuario {

    private final UUID id;
    private final String nome;
    private final Email email;
    private final String senhaHash;
    private final Role role;
    private boolean ativo;

    private Usuario(UUID id, String nome, Email email, String senhaHash, Role role, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.role = role;
        this.ativo = ativo;
    }

    /** Cria um novo usuário ativo com id gerado. senhaHash já deve vir cifrada. */
    public static Usuario registrar(String nome, Email email, String senhaHash, Role role) {
        if (nome == null || nome.isBlank()) {
            throw new BusinessRuleException("Nome é obrigatório");
        }
        if (senhaHash == null || senhaHash.isBlank()) {
            throw new BusinessRuleException("Senha é obrigatória");
        }
        if (role == null) {
            throw new BusinessRuleException("Papel (role) é obrigatório");
        }
        return new Usuario(UUID.randomUUID(), nome.trim(), email, senhaHash, role, true);
    }

    /** Reconstrói a partir da persistência, sem revalidar regras de criação. */
    public static Usuario reconstituir(UUID id, String nome, Email email, String senhaHash, Role role, boolean ativo) {
        return new Usuario(id, nome, email, senhaHash, role, ativo);
    }

    public void desativar() {
        this.ativo = false;
    }
}
