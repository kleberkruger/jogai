package br.ufms.jogai.domain.AI.shared;

import java.util.Optional;

/**
 * Normalização compartilhada de texto sem impor regras específicas de infraestrutura.
 */
public final class Texto {

    private Texto() {
    }

    public static String obrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " não pode ser nulo ou vazio");
        }
        return valor.strip();
    }

    public static Optional<String> opcional(String valor) {
        return Optional.ofNullable(valor).map(String::strip).filter(texto -> !texto.isEmpty());
    }
}
