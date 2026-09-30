package br.ufms.jogai.domain.AI.jogo;

import java.util.Optional;

public interface JogoRepository {

    Optional<Jogo> buscarPorId(JogoId id);

    Jogo salvar(Jogo jogo);
}
