package com.autobots.automanager.porta;

import com.autobots.automanager.entidade.Mercadoria;
import com.autobots.automanager.repositorio.RepositorioMercadoria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MercadoriaPortImpl implements MercadoriaPort {

    private final RepositorioMercadoria repositorio;

    @Override
    public List<MercadoriaInfo> buscarPorIds(List<Long> ids) {
        return repositorio.findAllById(ids).stream()
                .map(this::toInfo)
                .toList();
    }

    private MercadoriaInfo toInfo(Mercadoria m) {
        return new MercadoriaInfo(m.getId(), m.getNome(), m.getValor(), m.getQuantidade());
    }
}
