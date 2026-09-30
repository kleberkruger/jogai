package br.ufms.jogai.domain.jogo;

import br.ufms.jogai.domain.shared.Identificador;

import java.util.Objects;
import java.util.UUID;

public record JogoId(UUID valor) implements Identificador {

    public JogoId {
        Objects.requireNonNull(valor, "valor não pode ser nulo");
    }

    public static JogoId novo() {
        return new JogoId(UUID.randomUUID());
    }
}
