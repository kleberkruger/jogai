package br.ufms.jogai.domain.emprestimo;

public enum StatusEmprestimo {

    ATIVO("Empréstimo em andamento, dentro do prazo de devolução."),
    ATRASADO("Empréstimo em andamento após o prazo de devolução."),
    FINALIZADO("Todos os exemplares do empréstimo foram devolvidos."),
    CANCELADO("Empréstimo cancelado antes da devolução dos exemplares.");

    private final String descricao;

    StatusEmprestimo(String descricao) {
        this.descricao = descricao;
    }

    public String descricao() {
        return descricao;
    }
}
