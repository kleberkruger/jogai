package br.ufms.jogai.domain.AI.jogo;

import br.ufms.jogai.domain.AI.shared.Identificador;

import java.util.Objects;
import java.util.UUID;

public record ExemplarId(UUID valor) implements Identificador {

    public ExemplarId {
        Objects.requireNonNull(valor, "valor não pode ser nulo");
    }

    public static ExemplarId novo() {
        return new ExemplarId(UUID.randomUUID());
    }
}
