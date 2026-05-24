package com.autobots.automanager.seguranca.adaptadores;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.entidade.CredencialCodigoBarra;
import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.repositorio.RepositorioUsuario;

@Service
public class UserDetailsServiceCodigoBarraImpl {

    private final RepositorioUsuario repositorio;

    public UserDetailsServiceCodigoBarraImpl(RepositorioUsuario repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserByCodigo(long codigo) {
        Usuario usuario = repositorio.findByCredencialCodigoBarra(codigo)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado para código de barras: " + codigo));

        CredencialCodigoBarra credencial = usuario.getCredenciais().stream()
                .filter(c -> c instanceof CredencialCodigoBarra)
                .map(c -> (CredencialCodigoBarra) c)
                .filter(c -> c.getCodigo() == codigo)
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Credencial de código de barras não encontrada: " + codigo));

        return new UserDetailsCodigoBarraImpl(usuario, credencial);
    }
}
