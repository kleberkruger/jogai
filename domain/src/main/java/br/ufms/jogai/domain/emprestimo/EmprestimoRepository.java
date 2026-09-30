package br.ufms.jogai.domain.emprestimo;

import br.ufms.jogai.domain.usuario.UsuarioId;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EmprestimoRepository {

    Optional<Emprestimo> buscarPorId(EmprestimoId id);

    List<Emprestimo> listarEmAbertoPorUsuario(UsuarioId usuarioId);

    List<Emprestimo> listarAtrasadosEm(Instant instante);

    Emprestimo salvar(Emprestimo emprestimo);
}
