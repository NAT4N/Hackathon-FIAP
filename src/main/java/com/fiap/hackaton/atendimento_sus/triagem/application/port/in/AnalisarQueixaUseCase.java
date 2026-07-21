package com.fiap.hackaton.atendimento_sus.triagem.application.port.in;

import com.fiap.hackaton.atendimento_sus.triagem.application.port.out.AssistenteTriagemPort.AnaliseClinica;

/**
 * Caso de uso: analisar uma queixa em texto livre e sugerir sintomas
 * estruturados (pré-preenchimento da triagem). Não persiste nada.
 */
public interface AnalisarQueixaUseCase {

    AnaliseClinica analisar(String queixaLivre);
}
