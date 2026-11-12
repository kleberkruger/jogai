package br.ufms.jogai.data.model;

import br.ufms.jogai.data.model.shared.EntityDocument;
import br.ufms.jogai.data.model.shared.Unique;
import br.ufms.jogai.domain.jogo.CategoriaJogo;
import br.ufms.jogai.domain.jogo.Jogo;
import com.google.cloud.firestore.annotation.DocumentId;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Firestore-independent representation of a game document.
 */
public record JogoDocument(
        @DocumentId String id,
        @Unique String nome,
        String editora,
        Integer anoLancamento,
        int minimoJogadores,
        Integer maximoJogadores,
        Integer idadeMinima,
        Integer idadeMaxima,
        Long duracaoMinima,
        Long duracaoMaxima,
        Set<String> categorias,
        String descricao,
        boolean ativo
) implements EntityDocument<Jogo> {

    public JogoDocument(Jogo jogo) {
        this(
                jogo.getId().toString(),
                jogo.getNome(),
                jogo.getEditora(),
                jogo.getAnoLancamento().getValue(),
                jogo.getMinimoJogadores(),
                jogo.getMaximoJogadores(),
                jogo.getIdadeMinimaRecomendada(),
                jogo.getIdadeMaximaRecomendada(),
                jogo.getTempoMinimoEstimado().toMinutes(),
                jogo.getTempoMaximoEstimado().toMinutes(),
                jogo.getCategorias().stream()
                        .map(CategoriaJogo::getTag)
                        .collect(Collectors.toUnmodifiableSet()),
                jogo.getDescricao(),
                jogo.isAtivo()
        );
    }

    /**
     * Reconstitutes the game after its category documents have been resolved by the repository.
     */
    public Jogo toEntity(Set<CategoriaJogo> categoriasResolvidas) {
        return Jogo.reconstitute(UUID.fromString(id), ativo)
                .nomeEditoraLancamento(nome, editora, anoLancamento)
                .numeroJogadores(minimoJogadores, maximoJogadores)
                .faixaEtaria(idadeMinima, idadeMaxima)
                .duracaoPartida(Math.toIntExact(duracaoMinima), Math.toIntExact(duracaoMaxima))
                .categorias(categoriasResolvidas.toArray(CategoriaJogo[]::new))
                .descricao(descricao)
                .build();
    }

    /**
     * A game document cannot be reconstituted without resolving category IDs.
     */
    @Override
    public Jogo toEntity() {
        throw new IllegalStateException("Resolve category IDs through JogoFirestoreRepository before mapping to Jogo");
    }
}
