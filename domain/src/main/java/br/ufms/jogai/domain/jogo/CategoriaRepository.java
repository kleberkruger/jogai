package br.ufms.jogai.domain.jogo;

import java.util.Optional;

public interface CategoriaRepository {

    Optional<Categoria> buscarPorId(CategoriaId id);

    boolean existePorNome(String nome);

    Categoria salvar(Categoria categoria);
}
