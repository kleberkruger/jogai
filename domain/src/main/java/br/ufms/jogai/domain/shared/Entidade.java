package br.ufms.jogai.domain.shared;

import java.util.Objects;
import java.util.UUID;

public class Entidade {

    protected final UUID id;

    protected Entidade(UUID id) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Entidade entidade)) return false;
        return Objects.equals(id, entidade.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
