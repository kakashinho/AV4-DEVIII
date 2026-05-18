package com.autobots.automanager.servico;

import com.autobots.automanager.dto.requisicao.CredencialRequest;
import com.autobots.automanager.dto.requisicao.CredencialUpdateRequest;
import com.autobots.automanager.entidade.Credencial;

import java.util.List;

public interface CredencialService {
    Credencial criar(CredencialRequest request);
    Credencial buscarPorId(Long id);
    List<Credencial> listarTodos();
    Credencial atualizar(Long id, CredencialUpdateRequest request);
    void remover(Long id);
}
