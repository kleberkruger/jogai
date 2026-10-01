package br.ufms.jogai.domain.shared;

import java.util.Objects;
import java.util.UUID;

public class Entidade<ID> {

    protected final ID id;

    protected Entidade(ID id) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
    }

    public ID getId() {
        return id;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Entidade<?> entidade)) return false;
        return id.equals(entidade.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
