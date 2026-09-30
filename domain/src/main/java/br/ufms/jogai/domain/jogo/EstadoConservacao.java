package br.ufms.jogai.domain.jogo;

public enum EstadoConservacao {

    NOVO("Exemplar sem uso e em condição original"),
    EXCELENTE("Exemplar usado, sem sinais relevantes de desgaste"),
    BOM("Exemplar em boas condições, com sinais leves de uso"),
    REGULAR("Exemplar funcional com sinais visíveis de desgaste"),
    DANIFICADO("Exemplar com dano que exige avaliação ou manutenção");

    private final String descricao;

    EstadoConservacao(String descricao) {
        this.descricao = descricao;
    }

    public String descricao() {
        return descricao;
    }
}
