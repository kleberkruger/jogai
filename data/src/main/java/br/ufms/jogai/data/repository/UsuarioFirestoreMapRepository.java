package br.ufms.jogai.data.repository;

import br.ufms.jogai.data.firebase.FirestoreMapRepository;
import br.ufms.jogai.data.firebase.config.FirebaseConfig;
import br.ufms.jogai.domain.usuario.*;
import br.ufms.jogai.domain.shared.Page;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.DocumentSnapshot;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class UsuarioFirestoreMapRepository extends FirestoreMapRepository<Usuario, UUID> implements UsuarioRepository {

    public static final String COLLECTION_PATH = "usuarios";

    public UsuarioFirestoreMapRepository() {
        this(FirebaseConfig.getFirestore());
    }

    public UsuarioFirestoreMapRepository(Firestore firestore) {
        super(requireFirestore(firestore).collection(COLLECTION_PATH),
                Set.of("email", "telefone"));
    }

    /**
     * Cadastra todos os usuários atomicamente, incluindo suas reservas de email e telefone.
     */
    public CompletableFuture<Collection<Usuario>> cadastrarTodos(Collection<Usuario> usuarios) {
        Objects.requireNonNull(usuarios);
        return writeTransaction(writes -> usuarios.stream().map(usuario -> writes.create(this, usuario)).toList());
    }

    @Override
    protected Map<String, Object> entityToMap(Usuario entity) {
        return Map.of(
                "nome", entity.getNome(),
                "usuario", entity.getUsuario(),
                "email", entity.getEmail(),
                "telefone", entity.getTelefone(),
                "nascimento", toTimestamp(entity.getDataNascimento()),
                "avaliacao", Map.of(
                        "qtdAvaliacoesComodante", entity.getQuantidadeAvaliacoesComodante(),
                        "mediaAvaliacaoComodante", entity.getNotaAvaliacaoComodante(),
                        "qtdAvaliacoesComodatario", entity.getQuantidadeAvaliacoesComodatario(),
                        "mediaAvaliacaoComodatario", entity.getNotaAvaliacaoComodatario()
                ),
                "status", entity.getStatus().name(),
                "cadastradoEm", toTimestamp(entity.getDataCadastro())
        );
    }

    @Override
    protected CompletableFuture<Usuario> documentToEntity(DocumentSnapshot doc) {
        AvaliacaoMediaUsuario avaliacao = AvaliacaoMediaUsuario.reconstitute(
                requiredLong(doc, "avaliacao.qtdAvaliacoesComodante"),
                requiredDouble(doc, "avaliacao.mediaAvaliacaoComodante"),
                requiredLong(doc, "avaliacao.qtdAvaliacoesComodatario"),
                requiredDouble(doc, "avaliacao.mediaAvaliacaoComodatario")
        );

        return CompletableFuture.completedFuture(Usuario.reconstitute(
                requiredUUID(doc),
                requiredString(doc, "nome"),
                requiredString(doc, "usuario"),
                requiredString(doc, "email"),
                requiredString(doc, "telefone"),
                requiredLocalDate(doc, "nascimento"),
                avaliacao,
                StatusUsuario.valueOf(requiredString(doc, "status")),
                requiredInstant(doc, "cadastradoEm")
        ));
    }

    @Override
    public CompletableFuture<Page<Usuario, UUID>> listarPorNome(String nome, int pageSize, UUID afterId) {
        Objects.requireNonNull(nome);

        return queryPage(collection
                .whereGreaterThanOrEqualTo("nome", nome)
                .whereLessThanOrEqualTo("nome", nome + "\uf8ff")
                .orderBy("nome"), pageSize, afterId);
    }

    @Override
    public CompletableFuture<Optional<Usuario>> buscarPorUsuario(String usuario) {
        Objects.requireNonNull(usuario);
        return queryOne(collection.whereEqualTo("usuario", usuario));
    }

    @Override
    public CompletableFuture<Optional<Usuario>> buscarPorEmail(String email) {
        Objects.requireNonNull(email);
        return queryOne(collection.whereEqualTo("email", email));
    }

    @Override
    public CompletableFuture<Optional<Usuario>> buscarPorTelefone(String telefone) {
        Objects.requireNonNull(telefone);
        return queryOne(collection.whereEqualTo("telefone", telefone));
    }

    @Override
    public CompletableFuture<Page<Usuario, UUID>> listarPorStatus(StatusUsuario status, int pageSize, UUID afterId) {
        Objects.requireNonNull(status);
        return queryPage(collection.whereEqualTo("status", status.name()), pageSize, afterId);
    }
}
