package com.autobots.automanager.servico;

import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.entidade.Veiculo;
import com.autobots.automanager.excecao.ResourceNotFoundException;
import com.autobots.automanager.excecao.UsuarioNaoEncontradoException;
import com.autobots.automanager.excecao.VeiculoBloqueadoException;
import com.autobots.automanager.repositorio.RepositorioUsuario;
import com.autobots.automanager.repositorio.RepositorioVeiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VeiculoServiceImpl implements VeiculoService {

    @Autowired
    private RepositorioVeiculo repositorioVeiculo;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Override
    public Veiculo criarVeiculo(Veiculo veiculo) {
        return repositorioVeiculo.save(veiculo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Veiculo> listarVeiculos() {
        return repositorioVeiculo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Veiculo obterVeiculo(Long id) {
        return repositorioVeiculo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com id: " + id));
    }

    // PUT exige entidade completa: as 3 chamadas abaixo sempre recebem valores
    // validados pelo @Valid no controller — não há risco de sobrescrever com null.
    @Override
    public Veiculo atualizarVeiculo(Long id, Veiculo veiculoAtualizado) {
        Veiculo veiculo = obterVeiculo(id);
        veiculo.setTipo(veiculoAtualizado.getTipo());
        veiculo.setModelo(veiculoAtualizado.getModelo());
        veiculo.setPlaca(veiculoAtualizado.getPlaca());
        return repositorioVeiculo.save(veiculo);
    }

    // Bloqueia exclusão de veículo com proprietário ou vendas vinculadas.
    // Para excluir: dissociar do proprietário e remover/anular vendas primeiro.
    @Override
    public void excluirVeiculo(Long id) {
        Veiculo veiculo = obterVeiculo(id);
        boolean temProprietario = veiculo.getProprietario() != null;
        boolean temVendas = veiculo.getVendas() != null && !veiculo.getVendas().isEmpty();
        if (temProprietario || temVendas) {
            Long proprietarioId = temProprietario ? veiculo.getProprietario().getId() : null;
            List<Long> vendasIds = temVendas
                    ? veiculo.getVendas().stream().map(v -> v.getId()).collect(Collectors.toList())
                    : List.of();
            throw new VeiculoBloqueadoException(
                    "Veículo " + id + " não pode ser excluído enquanto possuir proprietário ou vendas associadas."
                    + (proprietarioId != null ? " Proprietário: " + proprietarioId + "." : "")
                    + (!vendasIds.isEmpty() ? " Vendas: " + vendasIds + "." : ""));
        }
        repositorioVeiculo.delete(veiculo);
    }

    // Transferência explícita de propriedade — única forma de trocar dono.
    @Override
    public Veiculo transferirProprietario(Long veiculoId, Long novoProprietarioId) {
        Veiculo veiculo = obterVeiculo(veiculoId);
        Usuario novo = repositorioUsuario.findById(novoProprietarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(novoProprietarioId));

        Usuario antigo = veiculo.getProprietario();
        if (antigo != null && antigo.getId().equals(novoProprietarioId)) {
            return veiculo;
        }
        if (antigo != null) {
            antigo.getVeiculos().remove(veiculo);
        }
        veiculo.setProprietario(novo);
        novo.getVeiculos().add(veiculo);
        return repositorioVeiculo.save(veiculo);
    }
}
