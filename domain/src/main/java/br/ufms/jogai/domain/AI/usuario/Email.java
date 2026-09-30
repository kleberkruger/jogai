package br.ufms.jogai.domain.AI.usuario;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/** Endereço de e-mail normalizado e validado pelo domínio. */
public record Email(String valor) {

    private static final Pattern FORMATO = Pattern.compile(
            "^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@"
                    + "(?:[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+"
                    + "[A-Za-z]{2,63}$");

    public Email {
        Objects.requireNonNull(valor, "email não pode ser nulo");
        valor = valor.strip().toLowerCase(Locale.ROOT);
        if (valor.length() > 254 || !FORMATO.matcher(valor).matches()) {
            throw new IllegalArgumentException("email inválido");
        }
    }

    @Override
    public String toString() { return valor; }
}
