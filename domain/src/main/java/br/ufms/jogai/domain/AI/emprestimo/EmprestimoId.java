package br.ufms.jogai.domain.AI.emprestimo;

import br.ufms.jogai.domain.AI.shared.Identificador;

import java.util.Objects;
import java.util.UUID;

public record EmprestimoId(UUID valor) implements Identificador {

    public EmprestimoId {
        Objects.requireNonNull(valor, "valor não pode ser nulo");
    }

    public static EmprestimoId novo() {
        return new EmprestimoId(UUID.randomUUID());
    }
}
