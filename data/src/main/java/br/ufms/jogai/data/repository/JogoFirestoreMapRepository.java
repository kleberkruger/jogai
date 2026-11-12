package br.ufms.jogai.data.repository;

import br.ufms.jogai.data.firebase.FirestoreMapRepository;
import br.ufms.jogai.data.firebase.config.FirebaseConfig;
import br.ufms.jogai.domain.jogo.CategoriaJogo;
import br.ufms.jogai.domain.jogo.Jogo;
import br.ufms.jogai.domain.jogo.JogoRepository;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Map-based Firestore persistence for games. Category IDs are resolved asynchronously on reads.
 */
public class JogoFirestoreMapRepository extends FirestoreMapRepository<Jogo, UUID>
        implements JogoRepository {

    public static final String COLLECTION_PATH = "jogos";

    public JogoFirestoreMapRepository() {
        this(FirebaseConfig.getFirestore());
    }

    public JogoFirestoreMapRepository(Firestore firestore) {
        super(requireFirestore(firestore).collection(COLLECTION_PATH), Set.of("nome"));
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
                        .map(c -> db.collection(CategoriaFirestoreMapRepository.COLLECTION_PATH).document(c.getTag()))
                        .toList()),
                Map.entry("descricao", entity.getDescricao()),
                Map.entry("ativo", entity.isAtivo())
        );
    }

    @Override
    protected CompletableFuture<Jogo> documentToEntity(DocumentSnapshot doc) {
        var categoriaRepository = new CategoriaFirestoreMapRepository(db);

        return categoriaRepository.getAllByReferences(optionalDocumentRefs(doc, "categorias"))
                .thenApply(resultado -> {
                    Set<CategoriaJogo> categorias = new LinkedHashSet<>(resultado);
                    return Jogo.reconstitute(
                                    requiredUUID(doc),
                                    requiredBoolean(doc, "ativo")
                            )
                            .nomeEditoraLancamento(requiredString(doc, "nome"),
                                    optionalString(doc, "editora"),
                                    requiredInt(doc, "anoLancamento"))
                            .numeroJogadores(requiredInt(doc, "minimoJogadores"),
                                    requiredInt(doc, "maximoJogadores"))
                            .faixaEtaria(requiredInt(doc, "idadeMinima"),
                                    requiredInt(doc, "idadeMaxima"))
                            .duracaoPartida(requiredInt(doc, "duracaoMinima"),
                                    requiredInt(doc, "duracaoMaxima"))
                            .categorias(categorias)
                            .descricao(optionalString(doc, "descricao"))
                            .build();
                });
    }
}
