package com.autobots.automanager.servico;

import java.util.List;

import com.autobots.automanager.dto.requisicao.MercadoriaRequest;
import com.autobots.automanager.dto.resposta.MercadoriaResponse;

public interface MercadoriaService {
    MercadoriaResponse cadastrar(MercadoriaRequest request);
    MercadoriaResponse buscarPorId(Long id);
    List<MercadoriaResponse> listarTodas();
    MercadoriaResponse atualizar(Long id, MercadoriaRequest request);
    void remover(Long id);
    MercadoriaResponse ajustarEstoque(Long id, long quantidadeMovimento, boolean entrada);
    void consumirEstoque(Long id, long quantidade);
}
