package br.ufms.jogai.domain.jogo;

import java.util.Set;
import java.util.UUID;

public record JogoInfo(UUID id, String nome, String editora, Set<CategoriaJogo> categorias) {

    public JogoInfo(Jogo jogo) {
        this(jogo.getId(), jogo.getNome(), jogo.getEditora(), jogo.getCategorias());
    }
}
