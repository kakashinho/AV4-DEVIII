package com.autobots.automanager.porta;

import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.repositorio.RepositorioUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioPortImpl implements UsuarioPort {

    private final RepositorioUsuario repositorio;

    @Override
    public Optional<UsuarioInfo> buscarPorId(Long id) {
        return repositorio.findById(id).map(this::toInfo);
    }

    private UsuarioInfo toInfo(Usuario u) {
        return new UsuarioInfo(u.getId(), u.getNome(), u.getPerfis());
    }
}
