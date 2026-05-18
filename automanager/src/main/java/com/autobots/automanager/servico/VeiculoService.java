package com.autobots.automanager.servico;

import com.autobots.automanager.entidade.Veiculo;
import java.util.List;

public interface VeiculoService {
    Veiculo criarVeiculo(Veiculo veiculo);
    List<Veiculo> listarVeiculos();
    Veiculo obterVeiculo(Long id);
    Veiculo atualizarVeiculo(Long id, Veiculo veiculoAtualizado);
    void excluirVeiculo(Long id);
    Veiculo transferirProprietario(Long veiculoId, Long novoProprietarioId);
}
