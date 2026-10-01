package br.ufms.jogai.domain.jogo;

public record FaixaEtaria(Integer idadeMinima, Integer idadeMaxima) {

    public FaixaEtaria {
        if (idadeMinima != null && idadeMinima < 0) {
            throw new IllegalArgumentException("A idade mínima não pode ser negativa");
        } else if (idadeMaxima != null && idadeMaxima < 0) {
            throw new IllegalArgumentException("A idade máxima não pode ser negativa");
        } else if (idadeMinima != null && idadeMaxima != null && idadeMaxima < idadeMinima) {
            throw new IllegalArgumentException("A idade máxima não pode ser menor que a idade mínima");
        }
    }

    /**
     * Cria uma faixa etária para todas as idades.
     */
    public FaixaEtaria() {
        this(null, null);
    }

    /**
     * Cria uma faixa etária com a idade mínima especificada e sem limite máximo.
     * Exemplos: 3+, 6+, 18+.
     *
     * @param idadeMinima idade mínima
     */
    public FaixaEtaria(Integer idadeMinima) {
        this(idadeMinima, null);
    }
}
