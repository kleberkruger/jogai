package br.ufms.jogai.domain.usuario;

import java.util.Optional;

/**
 * Porta de persistência do aggregate Usuario; implementações pertencem a módulos externos.
 */
public interface UsuarioRepository {

    Optional<Usuario> buscarPorId(UsuarioId id);

    boolean existePorEmail(Email email);

    Usuario salvar(Usuario usuario);
}
