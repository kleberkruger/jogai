package br.ufms.jogai.domain.AI.jogo;

public enum StatusExemplar {

    DISPONIVEL("Exemplar disponível para empréstimo"),
    EMPRESTADO("Exemplar emprestado a um usuário"),
    RESERVADO("Exemplar separado para atender uma reserva"),
    MANUTENCAO("Exemplar indisponível enquanto passa por manutenção"),
    INDISPONIVEL("Exemplar retirado permanentemente de circulação");

    private final String descricao;

    StatusExemplar(String descricao) {
        this.descricao = descricao;
    }

    public String descricao() {
        return descricao;
    }
}
