package br.ufms.jogai.domain.emprestimo;

import br.ufms.jogai.domain.jogo.Exemplar;
import br.ufms.jogai.domain.shared.Entidade;
import br.ufms.jogai.domain.usuario.UsuarioInfo;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public class Emprestimo extends Entidade<UUID> {

    private UsuarioInfo proprietario;
    private UsuarioInfo tomador;
    private Instant dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private Integer avaliacaoDoProprietario;
    private Integer avaliacaoDoTomador;

    private Collection<ItemEmprestimo> itens;

    protected Emprestimo(UUID uuid) {
        super(uuid);
    }

    public void adicionarItem(Exemplar exemplar) {

    }
}
