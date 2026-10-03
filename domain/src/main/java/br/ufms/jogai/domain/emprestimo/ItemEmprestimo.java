package br.ufms.jogai.domain.emprestimo;

import br.ufms.jogai.domain.jogo.EstadoConservacao;
import br.ufms.jogai.domain.jogo.Exemplar;

import java.time.Instant;

public class ItemEmprestimo {

    private Exemplar exemplar;
    private Instant dataEfetivaDevolucao;

    public void relatarDano(EstadoConservacao novoEstado, String texto) {
        exemplar.setEstadoConservacao(novoEstado);
    }
}
