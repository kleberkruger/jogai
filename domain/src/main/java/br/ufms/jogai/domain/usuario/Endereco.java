package br.ufms.jogai.domain.usuario;

public record Endereco(
        String logradouro,
        Integer numero,
        String bairro,
        String cidade,
        String estado
) {
}
