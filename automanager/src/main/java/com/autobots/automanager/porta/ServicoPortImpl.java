package com.autobots.automanager.porta;

import com.autobots.automanager.entidade.Servico;
import com.autobots.automanager.repositorio.RepositorioServico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ServicoPortImpl implements ServicoPort {

    private final RepositorioServico repositorio;

    @Override
    public List<ServicoInfo> buscarPorIds(List<Long> ids) {
        return repositorio.findAllById(ids).stream()
                .map(this::toInfo)
                .toList();
    }

    private ServicoInfo toInfo(Servico s) {
        return new ServicoInfo(s.getId(), s.getNome(), s.getValor());
    }
}
