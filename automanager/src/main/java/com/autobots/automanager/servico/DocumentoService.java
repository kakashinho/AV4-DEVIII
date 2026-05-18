package com.autobots.automanager.servico;

import com.autobots.automanager.dto.requisicao.DocumentoRequest;
import com.autobots.automanager.entidade.Documento;

import java.util.List;

public interface DocumentoService {
    Documento criar(DocumentoRequest request);
    Documento buscarPorId(Long id);
    List<Documento> listarTodos();
    Documento atualizar(Long id, DocumentoRequest request);
    void remover(Long id);
}
