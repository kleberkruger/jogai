package br.ufms.jogai.domain.jogo;

import br.ufms.jogai.domain.shared.Entidade;
import br.ufms.jogai.domain.usuario.UsuarioInfo;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Exemplar extends Entidade<UUID> {

    private final JogoInfo jogo;
    private final UsuarioInfo proprietario;
    private EstadoConservacao estadoConservacao;
    private final Instant dataCadastro;
    private String observacao;

    private Exemplar(
            UUID id,
            JogoInfo jogoInfo,
            UsuarioInfo usuarioInfo,
            EstadoConservacao estadoConservacao,
            Instant dataCadastro
    ) {
        super(id);

        this.jogo = Objects.requireNonNull(
                jogoInfo, "As informações do jogo não podem ser nulas");
        this.proprietario = Objects.requireNonNull(
                usuarioInfo, "As informações do proprietário do jogo não podem ser nulas");
        this.dataCadastro = Objects.requireNonNull(
                dataCadastro, "A data de cadastro não pode ser nula");

        setEstadoConservacao(estadoConservacao);
    }

    public JogoInfo getJogoInfo() {
        return jogo;
    }

    public UsuarioInfo getProprietarioInfo() {
        return proprietario;
    }

    public EstadoConservacao getEstadoConservacao() {
        return estadoConservacao;
    }

    public boolean isJogavel() {
        return estadoConservacao != EstadoConservacao.DANIFICADO;
    }

    public boolean isDanificado() {
        return estadoConservacao == EstadoConservacao.DANIFICADO;
    }

    public void setEstadoConservacao(EstadoConservacao estadoConservacao) {
        this.estadoConservacao = estadoConservacao;
    }

    public Instant getDataCadastro() {
        return dataCadastro;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao != null ? observacao.trim() : null;
    }

    public static Exemplar create(
            JogoInfo jogoInfo,
            UsuarioInfo usuarioInfo,
            EstadoConservacao estadoConservacao
    ) {
        return new Exemplar(UUID.randomUUID(), jogoInfo, usuarioInfo, estadoConservacao, Instant.now());
    }

    public static Exemplar reconstitute(
            UUID id,
            JogoInfo jogoInfo,
            UsuarioInfo usuarioInfo,
            EstadoConservacao estadoConservacao,
            Instant dataCadastro
    ) {
        return new Exemplar(id, jogoInfo, usuarioInfo, estadoConservacao, dataCadastro);
    }
}
