package com.autobots.automanager.mapeador;

import org.springframework.stereotype.Component;

import com.autobots.automanager.dto.requisicao.ServicoRequest;
import com.autobots.automanager.dto.resposta.ServicoResponse;
import com.autobots.automanager.entidade.Servico;

@Component
public class ServicoMapper {

    public Servico toEntity(ServicoRequest request) {
        Servico s = new Servico();
        s.setNome(request.getNome());
        s.setDescricao(request.getDescricao());
        s.setValor(request.getValor());
        return s;
    }

    public ServicoResponse toResponse(Servico servico) {
        ServicoResponse r = new ServicoResponse();
        r.setId(servico.getId());
        r.setNome(servico.getNome());
        r.setDescricao(servico.getDescricao());
        r.setValor(servico.getValor());
        return r;
    }

    public void atualizarEntidade(Servico entity, ServicoRequest request) {
        entity.setNome(request.getNome());
        entity.setDescricao(request.getDescricao());
        entity.setValor(request.getValor());
    }
}