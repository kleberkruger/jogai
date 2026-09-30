package br.ufms.jogai.domain.AI.usuario;

import br.ufms.jogai.domain.AI.shared.Identificador;

import java.util.Objects;
import java.util.UUID;

public record UsuarioId(UUID valor) implements Identificador {

    public UsuarioId {
        Objects.requireNonNull(valor, "valor não pode ser nulo");
    }

    public static UsuarioId novo() {
        return new UsuarioId(UUID.randomUUID());
    }
}
