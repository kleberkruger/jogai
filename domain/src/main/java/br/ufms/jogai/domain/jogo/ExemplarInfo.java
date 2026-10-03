package br.ufms.jogai.domain.jogo;

import java.util.UUID;

public record ExemplarInfo(
        UUID id,
        UUID idJogo,
        String nomeJogo,
        String editoraJogo,
        EstadoConservacao estadoConservacao,
        String observacao,
        UUID idProprietario,
        String nomePropretario,
        String emailPropretario
) {
}
