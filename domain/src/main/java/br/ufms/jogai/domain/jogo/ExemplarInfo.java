package br.ufms.jogai.domain.jogo;

import java.util.UUID;

public record ExemplarInfo(
        UUID id,
        UUID idProprietario,
        String nomePropretario,
        String emailPropretario,
        UUID idExemplar,
        String nomeJogo,
        String editoraJogo,
        EstadoConservacao estadoConservacao,
        String observacao
) {
}
