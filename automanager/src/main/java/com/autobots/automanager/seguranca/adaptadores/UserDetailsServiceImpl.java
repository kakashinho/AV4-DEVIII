package com.autobots.automanager.seguranca.adaptadores;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.entidade.CredencialUsuarioSenha;
import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.repositorio.RepositorioUsuario;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RepositorioUsuario repositorio;

    public UserDetailsServiceImpl(RepositorioUsuario repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String nomeUsuario) throws UsernameNotFoundException {
        Usuario usuario = repositorio.findByCredencialNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado: " + nomeUsuario));

        CredencialUsuarioSenha credencial = usuario.getCredenciais().stream()
                .filter(c -> c instanceof CredencialUsuarioSenha)
                .map(c -> (CredencialUsuarioSenha) c)
                .filter(c -> c.getNomeUsuario().equals(nomeUsuario))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Credencial não encontrada para: " + nomeUsuario));

        return new UserDetailsImpl(usuario, credencial);
    }
}
