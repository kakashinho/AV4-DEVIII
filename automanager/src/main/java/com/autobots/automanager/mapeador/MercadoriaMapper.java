package com.autobots.automanager.mapeador;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.autobots.automanager.dto.requisicao.MercadoriaRequest;
import com.autobots.automanager.dto.resposta.MercadoriaResponse;
import com.autobots.automanager.entidade.Mercadoria;

@Component
public class MercadoriaMapper {

    public Mercadoria toEntity(MercadoriaRequest request) {
        if (request == null) return null;

        Mercadoria m = new Mercadoria();
        m.setNome(request.getNome());
        m.setDescricao(request.getDescricao());
        m.setValor(request.getValor());
        m.setQuantidade(request.getQuantidade() != null ? request.getQuantidade() : 0L);
        m.setValidade(request.getValidade());
        m.setFabricacao(request.getFabricacao());
        m.setCadastro(LocalDateTime.now());
        m.setDisponivel(m.getQuantidade() > 0);
        return m;
    }

    public MercadoriaResponse toResponse(Mercadoria mercadoria) {
        if (mercadoria == null) return null;

        MercadoriaResponse r = new MercadoriaResponse();
        r.setId(mercadoria.getId());
        r.setNome(mercadoria.getNome());
        r.setDescricao(mercadoria.getDescricao());
        r.setValor(mercadoria.getValor());
        r.setQuantidadeEstoque(mercadoria.getQuantidade());
        r.setDisponivel(mercadoria.isDisponivel());
        r.setValidade(mercadoria.getValidade());
        r.setFabricacao(mercadoria.getFabricacao());
        r.setCadastro(mercadoria.getCadastro());
        return r;
    }

    public void atualizarEntidade(Mercadoria entity, MercadoriaRequest request) {
        if (entity == null || request == null) return;

        entity.setNome(request.getNome());
        entity.setDescricao(request.getDescricao());
        entity.setValor(request.getValor());
        entity.setValidade(request.getValidade());
        entity.setFabricacao(request.getFabricacao());
        if (request.getQuantidade() != null) {
            entity.setQuantidade(request.getQuantidade());
            entity.setDisponivel(request.getQuantidade() > 0);
        }
    }
}
