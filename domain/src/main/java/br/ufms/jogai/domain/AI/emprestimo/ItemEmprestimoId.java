package br.ufms.jogai.domain.AI.emprestimo;

import br.ufms.jogai.domain.AI.shared.Identificador;

import java.util.Objects;
import java.util.UUID;

public record ItemEmprestimoId(UUID valor) implements Identificador {

    public ItemEmprestimoId {
        Objects.requireNonNull(valor, "valor não pode ser nulo");
    }

    public static ItemEmprestimoId novo() {
        return new ItemEmprestimoId(UUID.randomUUID());
    }
}
