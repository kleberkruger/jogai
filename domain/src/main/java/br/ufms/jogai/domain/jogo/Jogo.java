package br.ufms.jogai.domain.jogo;

import br.ufms.jogai.domain.shared.Entidade;
import br.ufms.jogai.domain.util.Validar;

import java.time.Duration;
import java.time.Year;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Jogo extends Entidade<UUID> {
    private String nome;
    private String editora;
    private Year anoLancamento;
    private NumeroJogadores numeroJogadores;
    private FaixaEtaria faixaEtaria;
    private DuracaoPartida duracaoPartida;
    private final Set<CategoriaJogo> categorias;
    private String descricao;
    private boolean ativo;

    private Jogo(
            UUID id,
            String nome,
            String editora,
            Year anoLancamento,
            NumeroJogadores numeroJogadores,
            FaixaEtaria faixaEtaria,
            DuracaoPartida duracaoPartida,
            Set<CategoriaJogo> categorias,
            String descricao,
            boolean ativo
    ) {
        super(id);

        setNome(nome);
        setEditora(editora);
        setAnoLancamento(anoLancamento);
        setNumeroJogadores(numeroJogadores);
        setFaixaEtaria(faixaEtaria);
        setDuracaoPartida(duracaoPartida);
        setDescricao(descricao);

        if (categorias == null || categorias.isEmpty()) {
            throw new IllegalArgumentException("O jogo deve possuir pelo menos uma categoria");
        }
        this.categorias = new HashSet<>(categorias);
        this.ativo = ativo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = Validar.nomeJogo(nome);
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = Validar.nomeEditora(editora);
    }

    public Year getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(Year anoLancamento) {
        this.anoLancamento = Validar.anoLancamentoJogo(anoLancamento);
    }

//    public NumeroJogadores getNumeroJogadores() {
//        return numeroJogadores;
//    }

    public int getMinimoJogadores() {
        return numeroJogadores.minimo();
    }

    public Integer getMaximoJogadores() {
        return numeroJogadores.maximo();
    }

    void setNumeroJogadores(NumeroJogadores numeroJogadores) {
        this.numeroJogadores = numeroJogadores == null ? new NumeroJogadores() : numeroJogadores;
    }

    public FaixaEtaria getFaixaEtaria() {
        return faixaEtaria;
    }

    public Integer getIdadeMinimaRecomendada() {
        return faixaEtaria.idadeMinima();
    }

    public Integer getIdadeMaximaRecomendada() {
        return faixaEtaria.idadeMaxima();
    }

    public void setFaixaEtaria(FaixaEtaria faixaEtaria) {
        this.faixaEtaria = faixaEtaria == null ? new FaixaEtaria() : faixaEtaria;
    }

//    public DuracaoPartida getDuracaoPartida() {
//        return duracaoPartida;
//    }

    public Duration getTempoMinimoEstimado() {
        return duracaoPartida.minima();
    }

    public Duration getTempoMaximoEstimado() {
        return duracaoPartida.maxima();
    }

    public Duration getTempoEstimado() {
        return duracaoPartida.estimativa();
    }

    void setDuracaoPartida(DuracaoPartida duracaoPartida) {
        this.duracaoPartida = duracaoPartida == null ? new DuracaoPartida() : duracaoPartida;
    }

    public void adicionarCategoria(CategoriaJogo categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("A categoria não pode ser nula");
        }
        categorias.add(categoria);
    }

    public void adicionarCategorias(CategoriaJogo... categorias) {
        if (categorias == null) {
            throw new IllegalArgumentException("As categorias não podem ser nulas");
        }
        for (CategoriaJogo categoria : categorias) {
            adicionarCategoria(categoria);
        }
    }

    public void removerCategoria(CategoriaJogo categoria) {
        if (!categorias.contains(categoria)) {
            return;
        }
        if (categorias.size() == 1) {
            throw new IllegalStateException("O jogo deve possuir pelo menos uma categoria");
        }
        categorias.remove(categoria);
    }

    public void removerCategorias(CategoriaJogo... categorias) {
        if (categorias == null) {
            throw new IllegalArgumentException("As categorias não podem ser nulas");
        }
        Set<CategoriaJogo> aRemover = new HashSet<>(Arrays.asList(categorias));
        if (!this.categorias.containsAll(aRemover)) {
            aRemover.retainAll(this.categorias);
        }
        if (this.categorias.size() - aRemover.size() < 1) {
            throw new IllegalStateException("O jogo deve possuir pelo menos uma categoria");
        }
        this.categorias.removeAll(aRemover);
    }

    public Set<CategoriaJogo> getCategorias() {
        return Collections.unmodifiableSet(categorias);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao != null ? descricao.strip() : null;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void inativar() {
        this.ativo = false;
    }
}