package com.autobots.automanager.seguranca.adaptadores;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.autobots.automanager.entidade.CredencialCodigoBarra;
import com.autobots.automanager.entidade.Usuario;

public class UserDetailsCodigoBarraImpl implements UserDetails {

    private final Usuario usuario;
    private final CredencialCodigoBarra credencial;

    public UserDetailsCodigoBarraImpl(Usuario usuario, CredencialCodigoBarra credencial) {
        this.usuario = usuario;
        this.credencial = credencial;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return usuario.getPerfis().stream()
                .map(perfil -> new SimpleGrantedAuthority(perfil.name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    public Long getCredencialId() {
        return credencial.getId();
    }

    // Prefixo "CB:" distingue tokens de código de barras dos tokens de usuário/senha no JWT
    @Override
    public String getUsername() {
        return "CB:" + credencial.getCodigo();
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
