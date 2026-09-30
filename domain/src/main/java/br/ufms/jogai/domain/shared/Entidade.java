package br.ufms.jogai.domain.shared;

import java.util.Objects;
import java.util.UUID;

public class Entidade {

    protected UUID id;

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
