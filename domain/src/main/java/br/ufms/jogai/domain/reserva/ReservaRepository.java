package br.ufms.jogai.domain.reserva;

import br.ufms.jogai.domain.jogo.JogoId;
import br.ufms.jogai.domain.usuario.UsuarioId;

import java.util.List;
import java.util.Optional;

/**
 * A consulta da fila deve retornar reservas ativas em ordem de criação, da mais antiga para a mais nova.
 */
public interface ReservaRepository {

    Optional<Reserva> buscarPorId(ReservaId id);

    List<Reserva> listarAtivasPorJogoEmOrdemDeCriacao(JogoId jogoId);

    boolean existeAtivaPorUsuarioEJogo(UsuarioId usuarioId, JogoId jogoId);

    Reserva salvar(Reserva reserva);
}
