package br.ufms.jogai.domain.jogo;

import br.ufms.jogai.domain.shared.Repository;
import br.ufms.jogai.domain.usuario.Usuario;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface JogoRepository extends Repository<Jogo, UUID> {

    CompletableFuture<Collection<Usuario>> buscarExemplaresPorJogo(String nomeJogo);

    CompletableFuture<Collection<Usuario>> buscarExemplaresPorUsuario(String nomeUsuario);
}
