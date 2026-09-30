package br.ufms.jogai.domain.AI.jogo;

import java.util.List;
import java.util.Optional;

public interface ExemplarRepository {

    Optional<Exemplar> buscarPorId(ExemplarId id);

    List<Exemplar> listarPorJogo(JogoId jogoId);

    boolean existePorCodigo(String codigo);

    Exemplar salvar(Exemplar exemplar);
}
