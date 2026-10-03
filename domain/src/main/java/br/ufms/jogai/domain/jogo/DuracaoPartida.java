package br.ufms.jogai.domain.jogo;

import java.time.Duration;

/**
 * Representa a duração de uma partida, com duração mínima e máxima opcionais.
 *
 * @param minima duração mínima
 * @param maxima duração máxima
 */
public record DuracaoPartida(Duration minima, Duration maxima) {

    public DuracaoPartida {
        if (minima != null && minima.isNegative()) {
            throw new IllegalArgumentException("A duração mínima não pode ser negativa");
        } else if (maxima != null && maxima.isNegative()) {
            throw new IllegalArgumentException("A duração máxima não pode ser negativa");
        } else if (minima != null && maxima != null && maxima.compareTo(minima) < 0) {
            throw new IllegalArgumentException("A duração máxima não pode ser menor que a duração mínima");
        }
    }

    /**
     * Cria uma duração de partida sem limites especificados.
     */
    public DuracaoPartida() {
        this(null, null);
    }

    /**
     * Cria uma duração de partida com um valor estimado.
     *
     * @param estimativa duração estimada em minutos
     */
    public DuracaoPartida(int estimativa) {
        this(Duration.ofMinutes(estimativa));
    }

    /**
     * Cria uma duração de partida com um valor estimado.
     *
     * @param estimativa duração estimada
     */
    public DuracaoPartida(Duration estimativa) {
        this(estimativa, estimativa);
    }

    /**
     * Representa a duração de uma partida, com duração mínima e máxima opcionais.
     *
     * @param minima duração mínima
     * @param maxima duração máxima
     */
    public DuracaoPartida(int minima, int maxima) {
        this(Duration.ofMinutes(minima), Duration.ofMinutes(maxima));
    }

    public Duration estimativa() {
        if (minima == null) return maxima;
        if (maxima == null) return minima;

        return minima.plus(maxima.minus(minima).dividedBy(2));
    }
}
