package br.ufms.jogai.domain.emprestimo;

import br.ufms.jogai.domain.jogo.ExemplarId;
import br.ufms.jogai.domain.shared.Entidade;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Entidade interna ao aggregate Emprestimo; não é persistida nem alterada isoladamente.
 */
public final class ItemEmprestimo extends Entidade<ItemEmprestimoId> {
    
    private final ExemplarId exemplarId;
    private final Instant dataDevolucao;

    public ItemEmprestimo(ItemEmprestimoId id, ExemplarId exemplarId, Instant dataDevolucao) {
        super(id);
        this.exemplarId = Objects.requireNonNull(exemplarId, "exemplarId não pode ser nulo");
        this.dataDevolucao = dataDevolucao;
    }

    public ExemplarId exemplarId() {
        return exemplarId;
    }

    public Optional<Instant> dataDevolucao() {
        return Optional.ofNullable(dataDevolucao);
    }

    public boolean devolvido() {
        return dataDevolucao != null;
    }

    ItemEmprestimo devolver(Instant instante) {
        if (devolvido()) throw new IllegalStateException("o exemplar já foi devolvido");
        return new ItemEmprestimo(id(), exemplarId,
                Objects.requireNonNull(instante, "instante não pode ser nulo"));
    }
}
