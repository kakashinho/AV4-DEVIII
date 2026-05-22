package com.autobots.automanager.porta;

import java.util.Optional;

public interface VeiculoPort {
    Optional<VeiculoInfo> buscarPorId(Long id);
}
