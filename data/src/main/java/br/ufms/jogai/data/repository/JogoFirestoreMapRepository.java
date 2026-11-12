package br.ufms.jogai.data.repository;

import br.ufms.jogai.data.firebase.FirestoreMapRepository;
import br.ufms.jogai.data.firebase.config.FirebaseConfig;
import br.ufms.jogai.domain.jogo.CategoriaJogo;
import br.ufms.jogai.domain.jogo.CategoriaRepository;
import br.ufms.jogai.domain.jogo.Jogo;
import br.ufms.jogai.domain.jogo.JogoRepository;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Map-based Firestore persistence for games. Category IDs are resolved asynchronously on reads.
 */
public class JogoFirestoreMapRepository extends FirestoreMapRepository<Jogo, UUID>
        implements JogoRepository {

    public JogoFirestoreMapRepository() {
        this(FirebaseConfig.getFirestore());
    }

    public JogoFirestoreMapRepository(Firestore firestore) {
        super(Objects.requireNonNull(firestore).collection("jogos"), Set.of("nome"));
    }

    @Override
    protected Map<String, Object> entityToMap(Jogo entity) {
        return Map.ofEntries(
                Map.entry("nome", entity.getNome()),
                Map.entry("editora", entity.getEditora()),
                Map.entry("anoLancamento", entity.getAnoLancamento().getValue()),
                Map.entry("minimoJogadores", entity.getMinimoJogadores()),
                Map.entry("maximoJogadores", entity.getMaximoJogadores()),
                Map.entry("idadeMinima", entity.getIdadeMinimaRecomendada()),
                Map.entry("idadeMaxima", entity.getIdadeMaximaRecomendada()),
                Map.entry("duracaoMinima", entity.getTempoMinimoEstimado().toMinutes()),
                Map.entry("duracaoMaxima", entity.getTempoMaximoEstimado().toMinutes()),
                Map.entry("categorias", entity.getCategorias().stream()
                        .map(c -> db.collection("categoriasJogo").document(c.getTag()))
                        .toList()),
                Map.entry("descricao", entity.getDescricao()),
                Map.entry("ativo", entity.isAtivo())
        );
    }

    @Override
    protected Jogo documentToEntity(DocumentSnapshot doc) {
        Object valor = doc.get("categorias");

        List<DocumentReference> referencias = valor == null ? List.of() : ((List<?>) valor).stream()
                .map(DocumentReference.class::cast)
                .toList();

        var categoriaRepository = new CategoriaFirestoreMapRepository(db);
        Set<CategoriaJogo> categorias = new LinkedHashSet<>(categoriaRepository.getAllByReferences(referencias).join());

        return Jogo.reconstitute(
                        UUID.fromString(doc.getId()),
                        Objects.requireNonNull(doc.getBoolean("ativo")))
                .nomeEditoraLancamento(
                        doc.getString("nome"),
                        doc.getString("editora"),
                        Objects.requireNonNull(doc.getLong("anoLancamento")).intValue())
                .numeroJogadores(
                        Objects.requireNonNull(doc.getLong("minimoJogadores")).intValue(),
                        Objects.requireNonNull(doc.getLong("maximoJogadores")).intValue())
                .faixaEtaria(
                        Objects.requireNonNull(doc.getLong("idadeMinima")).intValue(),
                        Objects.requireNonNull(doc.getLong("idadeMaxima")).intValue())
                .duracaoPartida(
                        Objects.requireNonNull(doc.getLong("duracaoMinima")).intValue(),
                        Objects.requireNonNull(doc.getLong("duracaoMaxima")).intValue())
                .categorias(categorias)
                .descricao(doc.getString("descricao"))
                .build();
    }

    @Override
    protected CompletableFuture<Jogo> documentToEntityAsync(DocumentSnapshot doc) {
        Object valor = doc.get("categorias");

        List<DocumentReference> referencias = valor == null ? List.of() : ((List<?>) valor).stream()
                .map(DocumentReference.class::cast)
                .toList();

        var categoriaRepository = new CategoriaFirestoreMapRepository(db);

        return categoriaRepository.getAllByReferences(referencias)
                .thenApply(resultado -> {
                    Set<CategoriaJogo> categorias = new LinkedHashSet<>(resultado);

                    return Jogo.reconstitute(
                                    UUID.fromString(doc.getId()),
                                    Objects.requireNonNull(doc.getBoolean("ativo")))
                            .nomeEditoraLancamento(
                                    doc.getString("nome"),
                                    doc.getString("editora"),
                                    Objects.requireNonNull(doc.getLong("anoLancamento")).intValue())
                            .numeroJogadores(
                                    Objects.requireNonNull(doc.getLong("minimoJogadores")).intValue(),
                                    Objects.requireNonNull(doc.getLong("maximoJogadores")).intValue())
                            .faixaEtaria(
                                    Objects.requireNonNull(doc.getLong("idadeMinima")).intValue(),
                                    Objects.requireNonNull(doc.getLong("idadeMaxima")).intValue())
                            .duracaoPartida(
                                    Objects.requireNonNull(doc.getLong("duracaoMinima")).intValue(),
                                    Objects.requireNonNull(doc.getLong("duracaoMaxima")).intValue())
                            .categorias(categorias)
                            .descricao(doc.getString("descricao"))
                            .build();
                });
    }
}
