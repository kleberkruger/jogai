package br.ufms.jogai.domain.reserva;

import br.ufms.jogai.domain.shared.Identificador;

import java.util.Objects;
import java.util.UUID;

public record ReservaId(UUID valor) implements Identificador {

    public ReservaId {
        Objects.requireNonNull(valor, "valor não pode ser nulo");
    }

    public static ReservaId novo() {
        return new ReservaId(UUID.randomUUID());
    }
}
