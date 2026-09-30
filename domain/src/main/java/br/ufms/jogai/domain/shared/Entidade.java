package br.ufms.jogai.domain.shared;

import java.util.Objects;

/**
 * Igualdade de entidades é determinada por identidade, não por seus atributos mutáveis.
 */
public abstract class Entidade<ID extends Identificador> {

    private final ID id;

    protected Entidade(ID id) {
        this.id = Objects.requireNonNull(id, "id não pode ser nulo");
    }

    public final ID id() {
        return id;
    }

    @Override
    public final boolean equals(Object other) {
        return this == other || (other != null && getClass() == other.getClass()
                && id.equals(((Entidade<?>) other).id));
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass(), id);
    }
}
