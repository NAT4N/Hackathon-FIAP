package com.fiap.hackaton.atendimento_sus.paciente.domain.model;

import com.fiap.hackaton.atendimento_sus.shared.exception.BusinessRuleException;
import lombok.Getter;

import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

/** Paciente do SUS. Identificado por CPF (único) e Cartão SUS. */
@Getter
public class Paciente {

    private final UUID id;
    private final String nome;
    private final Cpf cpf;
    private final LocalDate dataNascimento;
    private final Sexo sexo;
    private final String telefone;
    private final CartaoSus cartaoSus;

    private Paciente(UUID id, String nome, Cpf cpf, LocalDate dataNascimento,
                     Sexo sexo, String telefone, CartaoSus cartaoSus) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.telefone = telefone;
        this.cartaoSus = cartaoSus;
    }

    public static Paciente cadastrar(String nome, Cpf cpf, LocalDate dataNascimento,
                                     Sexo sexo, String telefone, CartaoSus cartaoSus, Clock clock) {
        if (nome == null || nome.isBlank()) {
            throw new BusinessRuleException("Nome é obrigatório");
        }
        if (cpf == null) {
            throw new BusinessRuleException("CPF é obrigatório");
        }
        if (sexo == null) {
            throw new BusinessRuleException("Sexo é obrigatório");
        }
        if (dataNascimento == null) {
            throw new BusinessRuleException("Data de nascimento é obrigatória");
        }
        if (dataNascimento.isAfter(LocalDate.now(clock))) {
            throw new BusinessRuleException("Data de nascimento não pode ser no futuro");
        }
        return new Paciente(UUID.randomUUID(), nome.trim(), cpf, dataNascimento, sexo, telefone, cartaoSus);
    }

    public static Paciente reconstituir(UUID id, String nome, Cpf cpf, LocalDate dataNascimento,
                                        Sexo sexo, String telefone, CartaoSus cartaoSus) {
        return new Paciente(id, nome, cpf, dataNascimento, sexo, telefone, cartaoSus);
    }
}
