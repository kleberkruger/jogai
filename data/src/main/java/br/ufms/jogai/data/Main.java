package br.ufms.jogai.data;

import br.ufms.jogai.data.model.UsuarioDocument;
import br.ufms.jogai.data.repository.UsuarioRepository;

import java.time.LocalDate;

public class Main {

    static void main() throws Exception {
        try {
            var usuario = new UsuarioDocument(
                    "Kleber Kruger",
                    "kleberkruger@gmail.com",
                    "67996122809",
                    LocalDate.of(1988, 12, 6),
                    "Ativo");

            new UsuarioRepository().save(usuario).get();
            System.out.println("Usuário gravado no Firestore.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
