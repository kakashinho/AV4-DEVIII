package com.autobots.automanager.servico;

import java.util.List;

import com.autobots.automanager.dto.requisicao.MercadoriaRequest;
import com.autobots.automanager.dto.requisicao.MercadoriaUpdateRequest;
import com.autobots.automanager.dto.resposta.MercadoriaResponse;

public interface MercadoriaService {
    MercadoriaResponse cadastrar(MercadoriaRequest request);
    MercadoriaResponse buscarPorId(Long id);
    List<MercadoriaResponse> listarTodas();
    MercadoriaResponse atualizar(Long id, MercadoriaUpdateRequest request);
    void remover(Long id);
    MercadoriaResponse ajustarEstoque(Long id, long quantidadeMovimento, boolean entrada);
    void consumirEstoque(Long id, long quantidade);

    // Usado por EmpresaService ao remover uma empresa
    void desassociarEmpresa(Long mercadoriaId);
}
