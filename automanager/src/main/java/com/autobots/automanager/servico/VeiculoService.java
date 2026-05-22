package com.autobots.automanager.servico;

import com.autobots.automanager.dto.requisicao.VeiculoRequest;
import com.autobots.automanager.entidade.Veiculo;
import java.util.List;

public interface VeiculoService {
    Veiculo criarVeiculo(VeiculoRequest request);
    List<Veiculo> listarVeiculos();
    Veiculo obterVeiculo(Long id);
    Veiculo atualizarVeiculo(Long id, Veiculo veiculoAtualizado);
    void excluirVeiculo(Long id);
    Veiculo transferirProprietario(Long veiculoId, Long novoProprietarioId);
    void removerProprietario(Long veiculoId);
}
