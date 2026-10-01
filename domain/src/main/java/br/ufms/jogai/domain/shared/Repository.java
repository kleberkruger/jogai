package br.ufms.jogai.domain.shared;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface Repository<Entity extends Entidade<? extends Id>, Id extends Serializable> {

    CompletableFuture<Entity> save(Entity entity);

    CompletableFuture<Void> delete(Id id);

    CompletableFuture<Optional<Entity>> get(Id id);

    CompletableFuture<Collection<Entity>> getAll();
}
