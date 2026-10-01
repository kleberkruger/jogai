package br.ufms.jogai.domain.jogo;

public enum EstadoConservacao {

    NOVO("Novo", "Exemplar sem uso e em condição original"),
    EXCELENTE("Excelente", "Exemplar usado, sem sinais relevantes de desgaste"),
    BOM("Bom", "Exemplar em boas condições, com sinais leves de uso"),
    REGULAR("Regular", "Exemplar funcional com sinais visíveis de desgaste"),
    DANIFICADO("Danificado", "Exemplar com dano que exige avaliação ou manutenção");

    private final String nome;
    private final String descricao;

    EstadoConservacao(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String nome() {
        return nome;
    }

    public String descricao() {
        return descricao;
    }
}
