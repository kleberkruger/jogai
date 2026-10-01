package br.ufms.jogai.domain.jogo;

/**
 * Cria uma quantidade de jogadores com o mínimo e o máximo informados.
 *
 * @param minimo mínimo de jogadores
 * @param maximo máximo de jogadores
 */
public record NumeroJogadores(int minimo, Integer maximo) {
    public NumeroJogadores {
        if (minimo < 1) {
            throw new IllegalArgumentException("O mínimo de jogadores deve ser 1");
        }
        if (maximo != null && maximo < minimo) {
            throw new IllegalArgumentException("O máximo de jogadores não pode ser menor que o mínimo");
        }
    }

    /**
     * Cria uma quantidade de jogadores sem limite máximo definido.
     */
    public NumeroJogadores() {
        this(1, null);
    }

    /**
     * Cria uma quantidade de jogadores com o mínimo de 1 e o máximo informado.
     *
     * @param maximo máximo de jogadores
     */
    public NumeroJogadores(Integer maximo) {
        this(1, maximo);
    }
}
