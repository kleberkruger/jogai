package br.ufms.jogai.domain.jogo;

import br.ufms.jogai.domain.shared.Identificador;

import java.util.Objects;
import java.util.UUID;

public record CategoriaId(UUID valor) implements Identificador {

    public CategoriaId {
        Objects.requireNonNull(valor, "valor não pode ser nulo");
    }

    public static CategoriaId novo() {
        return new CategoriaId(UUID.randomUUID());
    }
}
