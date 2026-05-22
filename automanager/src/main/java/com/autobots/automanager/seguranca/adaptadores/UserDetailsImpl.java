package com.autobots.automanager.seguranca.adaptadores;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.autobots.automanager.entidade.CredencialUsuarioSenha;
import com.autobots.automanager.entidade.Usuario;

public class UserDetailsImpl implements UserDetails {

    private final Usuario usuario;
    private final CredencialUsuarioSenha credencial;

    public UserDetailsImpl(Usuario usuario, CredencialUsuarioSenha credencial) {
        this.usuario = usuario;
        this.credencial = credencial;
    }

    public Long getUsuarioId() {
        return usuario.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> autoridades = usuario.getPerfis().stream()
                .map(perfil -> new SimpleGrantedAuthority(perfil.name()))
                .toList();
        return autoridades;
    }

    @Override
    public String getPassword() {
        return credencial.getSenha();
    }

    @Override
    public String getUsername() {
        return credencial.getNomeUsuario();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !credencial.isInativo();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !credencial.isInativo();
    }
}
