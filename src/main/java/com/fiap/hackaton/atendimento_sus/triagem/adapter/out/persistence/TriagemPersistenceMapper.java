package com.fiap.hackaton.atendimento_sus.triagem.adapter.out.persistence;

import com.fiap.hackaton.atendimento_sus.triagem.domain.model.SinaisVitais;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.Sintoma;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.Triagem;

import java.util.EnumSet;
import java.util.Set;

final class TriagemPersistenceMapper {

    private TriagemPersistenceMapper() {
    }

    static TriagemJpaEntity toEntity(Triagem t) {
        SinaisVitais sv = t.getSinaisVitais();
        TriagemJpaEntity e = new TriagemJpaEntity();
        e.setId(t.getId());
        e.setPacienteId(t.getPacienteId());
        e.setProfissionalId(t.getProfissionalId());
        e.setFrequenciaCardiaca(sv.frequenciaCardiaca());
        e.setFrequenciaRespiratoria(sv.frequenciaRespiratoria());
        e.setPressaoSistolica(sv.pressaoSistolica());
        e.setPressaoDiastolica(sv.pressaoDiastolica());
        e.setTemperatura(sv.temperatura());
        e.setSaturacaoOxigenio(sv.saturacaoOxigenio());
        e.setEscalaDor(sv.escalaDor());
        e.setNivelRisco(t.getNivelRisco());
        e.setCriadoEm(t.getCriadoEm());
        e.setOrientacao(t.getOrientacao());
        e.setSintomas(t.getSintomas().isEmpty()
                ? EnumSet.noneOf(Sintoma.class) : EnumSet.copyOf(t.getSintomas()));
        return e;
    }

    static Triagem toDomain(TriagemJpaEntity e) {
        SinaisVitais sv = new SinaisVitais(
                e.getFrequenciaCardiaca(),
                e.getFrequenciaRespiratoria(),
                e.getPressaoSistolica(),
                e.getPressaoDiastolica(),
                e.getTemperatura(),
                e.getSaturacaoOxigenio(),
                e.getEscalaDor());
        Set<Sintoma> sintomas = (e.getSintomas() == null || e.getSintomas().isEmpty())
                ? EnumSet.noneOf(Sintoma.class) : EnumSet.copyOf(e.getSintomas());
        return Triagem.reconstituir(e.getId(), e.getPacienteId(), e.getProfissionalId(),
                sv, sintomas, e.getNivelRisco(), e.getCriadoEm(), e.getOrientacao());
    }
}
