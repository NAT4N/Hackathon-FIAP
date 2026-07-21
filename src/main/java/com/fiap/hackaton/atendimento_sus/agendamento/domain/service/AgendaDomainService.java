package com.fiap.hackaton.atendimento_sus.agendamento.domain.service;

import com.fiap.hackaton.atendimento_sus.agendamento.domain.model.Agendamento;

import java.util.List;
import java.util.UUID;

/**
 * Serviço de domínio puro que decide se um novo agendamento conflita com os
 * agendamentos ativos existentes. O <i>fetch</i> dos existentes é
 * responsabilidade de uma porta; aqui mora apenas a decisão.
 */
public class AgendaDomainService {

    /**
     * Há conflito quando existe outro agendamento ativo do <b>mesmo
     * profissional</b> cuja janela de horário sobrepõe a do novo.
     *
     * @param novo               agendamento pretendido
     * @param existentesAtivos   agendamentos já registrados (ativos ou não; filtramos aqui)
     */
    public boolean haConflito(Agendamento novo, List<Agendamento> existentesAtivos) {
        if (novo == null || existentesAtivos == null) {
            return false;
        }
        return existentesAtivos.stream()
                .filter(existente -> !existente.getId().equals(novo.getId()))
                .filter(existente -> existente.getStatus().ativo())
                .filter(existente -> existente.getProfissionalId().equals(novo.getProfissionalId()))
                .anyMatch(existente -> existente.getJanela().sobrepoe(novo.getJanela()));
    }
}
