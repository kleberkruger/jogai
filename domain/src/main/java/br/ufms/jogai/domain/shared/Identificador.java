package br.ufms.jogai.domain.shared;

import java.util.UUID;

/**
 * Identificador tipado de um conceito do domínio.
 */
public interface Identificador {

    UUID valor();
}
