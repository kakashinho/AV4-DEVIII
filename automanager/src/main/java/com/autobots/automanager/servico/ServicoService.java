package com.autobots.automanager.servico;

import java.util.List;

import com.autobots.automanager.dto.requisicao.ServicoRequest;
import com.autobots.automanager.dto.resposta.ServicoResponse;

public interface ServicoService {

    List<ServicoResponse> listarTodos();

    ServicoResponse buscarPorId(Long id);

    ServicoResponse cadastrar(ServicoRequest request);

    ServicoResponse atualizar(Long id, ServicoRequest request);

    void remover(Long id);
}