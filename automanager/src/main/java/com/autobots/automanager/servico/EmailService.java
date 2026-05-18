package com.autobots.automanager.servico;

import com.autobots.automanager.dto.requisicao.EmailRequest;
import com.autobots.automanager.entidade.Email;

import java.util.List;

public interface EmailService {
    Email criar(EmailRequest request);
    Email buscarPorId(Long id);
    List<Email> listarTodos();
    Email atualizar(Long id, EmailRequest request);
    void remover(Long id);
}
