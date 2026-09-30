package br.ufms.jogai.domain.jogo;

import br.ufms.jogai.domain.shared.Entidade;
import br.ufms.jogai.domain.shared.Texto;

import java.util.Optional;

/**
 * Aggregate root de classificação; jogos guardam sua identidade, não a entidade inteira.
 */
public final class Categoria extends Entidade<CategoriaId> {

    private final String nome;
    private final String descricao;

    public Categoria(CategoriaId id, String nome, String descricao) {
        super(id);
        this.nome = Texto.obrigatorio(nome, "nome");
        this.descricao = Texto.opcional(descricao).orElse(null);
    }

    public String nome() {
        return nome;
    }

    public Optional<String> descricao() {
        return Optional.ofNullable(descricao);
    }
}
