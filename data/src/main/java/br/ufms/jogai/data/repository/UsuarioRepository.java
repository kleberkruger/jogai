package br.ufms.jogai.data.repository;

import br.ufms.jogai.data.firebase.FirestoreRepository;
import br.ufms.jogai.data.model.UsuarioDocument;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class UsuarioRepository extends FirestoreRepository<UsuarioDocument, String> {

    public UsuarioRepository() {
        super(db.collection("usuarios"));
    }

    @Override
    protected Optional<String> getId(UsuarioDocument usuarioDocument) {
        return Optional.empty();
    }

    @Override
    protected Map<String, Object> entityToMap(UsuarioDocument usuarioDocument) {
        return Map.of(
                "nome", usuarioDocument.nome(),
                "email", usuarioDocument.email(),
                "telefone", usuarioDocument.telefone(),
                // Firestore's Java POJO mapper treats java.time.LocalDate as a bean;
                // persist this date-only value in an unambiguous ISO-8601 form instead.
                "nascimento", usuarioDocument.nascimento().toString(),
                "status", usuarioDocument.status()
        );
    }

    @Override
    protected CompletableFuture<UsuarioDocument> documentToEntity(DocumentSnapshot document) {
        return CompletableFuture.completedFuture(new UsuarioDocument(
                document.getString("nome"),
                document.getString("email"),
                document.getString("telefone"),
                LocalDate.parse(document.getString("nascimento")),
                document.getString("status")
        ));
    }
}
