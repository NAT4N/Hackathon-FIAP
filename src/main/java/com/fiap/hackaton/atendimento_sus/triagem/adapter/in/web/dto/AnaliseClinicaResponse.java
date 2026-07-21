package com.fiap.hackaton.atendimento_sus.triagem.adapter.in.web.dto;

import com.fiap.hackaton.atendimento_sus.triagem.application.port.out.AssistenteTriagemPort.AnaliseClinica;
import com.fiap.hackaton.atendimento_sus.triagem.domain.model.Sintoma;

import java.util.Set;

/** Sugestão da IA: sintomas estruturados + resumo. Não é uma triagem persistida. */
public record AnaliseClinicaResponse(Set<Sintoma> sintomasSugeridos, String resumo) {

    public static AnaliseClinicaResponse de(AnaliseClinica a) {
        return new AnaliseClinicaResponse(a.sintomasSugeridos(), a.resumo());
    }
}
