package br.ufms.jogai.domain.usuario;

public enum StatusUsuario {

    ATIVO("Usuário ativo e apto a realizar empréstimos."),
    BLOQUEADO("Usuário bloqueado, impedido de realizar novos empréstimos."),
    INATIVO("Usuário inativo, sem permissão para realizar operações.");

    private final String descricao;

    StatusUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String descricao() {
        return descricao;
    }
}
