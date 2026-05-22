package com.autobots.automanager.porta;

import java.util.Optional;

public interface UsuarioPort {
    Optional<UsuarioInfo> buscarPorId(Long id);
}
