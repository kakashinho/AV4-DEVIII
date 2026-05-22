package com.autobots.automanager.porta;

import com.autobots.automanager.entidade.Veiculo;
import com.autobots.automanager.repositorio.RepositorioVeiculo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VeiculoPortImpl implements VeiculoPort {

    private final RepositorioVeiculo repositorio;

    @Override
    public Optional<VeiculoInfo> buscarPorId(Long id) {
        return repositorio.findById(id).map(this::toInfo);
    }

    private VeiculoInfo toInfo(Veiculo v) {
        Long proprietarioId = (v.getProprietario() != null) ? v.getProprietario().getId() : null;
        return new VeiculoInfo(v.getId(), v.getPlaca(), v.getModelo(), v.getTipo(), proprietarioId);
    }
}
