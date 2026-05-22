package com.autobots.automanager.servico;

import com.autobots.automanager.dto.requisicao.EnderecoRequest;
import com.autobots.automanager.dto.resposta.EnderecoResponse;

import java.util.List;

public interface EnderecoService {
    EnderecoResponse cadastrar(EnderecoRequest request);
    EnderecoResponse buscarPorId(Long id);
    List<EnderecoResponse> listarTodos();
    EnderecoResponse atualizar(Long id, EnderecoRequest request);
    void remover(Long id);
}
