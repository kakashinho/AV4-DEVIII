package com.autobots.automanager.servico;

import com.autobots.automanager.dto.requisicao.TelefoneRequest;
import com.autobots.automanager.entidade.Telefone;

import java.util.List;

public interface TelefoneService {
    Telefone criar(TelefoneRequest request);
    Telefone buscarPorId(Long id);
    List<Telefone> listarTodos();
    Telefone atualizar(Long id, TelefoneRequest request);
    void remover(Long id);
}
