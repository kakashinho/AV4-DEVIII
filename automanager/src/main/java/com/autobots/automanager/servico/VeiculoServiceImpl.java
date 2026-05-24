package com.autobots.automanager.servico;

import com.autobots.automanager.dto.requisicao.VeiculoRequest;
import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.entidade.Veiculo;
import com.autobots.automanager.excecao.RecursoJaVinculadoException;
import com.autobots.automanager.excecao.ResourceNotFoundException;
import com.autobots.automanager.excecao.UsuarioNaoEncontradoException;
import com.autobots.automanager.excecao.VeiculoBloqueadoException;
import com.autobots.automanager.mapeador.VeiculoMapper;
import com.autobots.automanager.repositorio.RepositorioUsuario;
import com.autobots.automanager.repositorio.RepositorioVeiculo;
import com.autobots.automanager.repositorio.RepositorioVenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VeiculoServiceImpl implements VeiculoService {

    @Autowired private RepositorioVeiculo repositorioVeiculo;
    @Autowired private RepositorioUsuario repositorioUsuario;
    @Autowired private RepositorioVenda repositorioVenda;
    @Autowired private VeiculoMapper veiculoMapper;

    @Override
    public Veiculo criarVeiculo(VeiculoRequest request) {
        if (repositorioVeiculo.existsByPlaca(request.getPlaca())) {
            throw new RecursoJaVinculadoException(
                    "Já existe um veículo com a placa '" + request.getPlaca() + "'.");
        }
        Usuario proprietario = repositorioUsuario.findById(request.getProprietarioId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(request.getProprietarioId()));
        return repositorioVeiculo.save(veiculoMapper.toEntity(request, proprietario));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Veiculo> listarVeiculos() {
        return repositorioVeiculo.findAllComProprietario();
    }

    @Override
    @Transactional(readOnly = true)
    public Veiculo obterVeiculo(Long id) {
        return repositorioVeiculo.findByIdComProprietario(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com id: " + id));
    }

    // PUT exige entidade completa: as 3 chamadas abaixo sempre recebem valores
    // validados pelo @Valid no controller — não há risco de sobrescrever com null.
    @Override
    public Veiculo atualizarVeiculo(Long id, Veiculo veiculoAtualizado) {
        Veiculo veiculo = obterVeiculo(id);
        if (repositorioVeiculo.existsByPlacaAndIdNot(veiculoAtualizado.getPlaca(), id)) {
            throw new RecursoJaVinculadoException(
                    "Já existe um veículo com a placa '" + veiculoAtualizado.getPlaca() + "'.");
        }
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
        boolean temVendas = repositorioVenda.existsByVeiculoId(id);
        if (temProprietario || temVendas) {
            Long proprietarioId = temProprietario ? veiculo.getProprietario().getId() : null;
            throw new VeiculoBloqueadoException(
                    "Veículo " + id + " não pode ser excluído enquanto possuir proprietário ou vendas associadas."
                    + (proprietarioId != null ? " Proprietário: " + proprietarioId + "." : "")
                    + (temVendas ? " Possui vendas vinculadas." : ""));
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

        veiculo.setProprietario(novo);
        return repositorioVeiculo.save(veiculo);
    }

    @Override
    public void removerProprietario(Long veiculoId) {
        Veiculo veiculo = obterVeiculo(veiculoId);
        veiculo.setProprietario(null);
        repositorioVeiculo.save(veiculo);
    }
}
