package br.ufms.jogai.data.model;

import java.time.LocalDate;

public record UsuarioDocument(
        String nome,
        String email,
        String telefone,
        LocalDate nascimento,
        String status
) {
}
