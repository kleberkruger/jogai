package br.ufms.jogai.domain.shared;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface Repository<E extends Entidade<ID>, ID extends Serializable> {

    CompletableFuture<E> save(E entity);

    CompletableFuture<Void> delete(ID id);

    CompletableFuture<Optional<E>> get(ID id);

    CompletableFuture<Collection<E>> getAll();
}
