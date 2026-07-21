package com.fiap.hackaton.atendimento_sus.triagem.domain.model;

/**
 * Níveis de risco no estilo Protocolo de Manchester. A prioridade (1 = mais
 * grave) e o tempo-alvo de atendimento acompanham cada nível.
 */
public enum NivelRisco {

    VERMELHO(1, "Emergência", 0),
    LARANJA(2, "Muito urgente", 10),
    AMARELO(3, "Urgente", 60),
    VERDE(4, "Pouco urgente", 120),
    AZUL(5, "Não urgente", 240);

    private final int prioridade;
    private final String descricao;
    private final int tempoAlvoMinutos;

    NivelRisco(int prioridade, String descricao, int tempoAlvoMinutos) {
        this.prioridade = prioridade;
        this.descricao = descricao;
        this.tempoAlvoMinutos = tempoAlvoMinutos;
    }

    public int prioridade() {
        return prioridade;
    }

    public String descricao() {
        return descricao;
    }

    public int tempoAlvoMinutos() {
        return tempoAlvoMinutos;
    }
}
