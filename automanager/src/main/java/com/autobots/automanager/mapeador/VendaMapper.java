package com.autobots.automanager.mapeador;

import com.autobots.automanager.dto.resposta.ItemServicoResponse;
import com.autobots.automanager.dto.resposta.ItemVendaResponse;
import com.autobots.automanager.dto.resposta.VendaResponse;
import com.autobots.automanager.entidade.ItemServico;
import com.autobots.automanager.entidade.ItemVenda;
import com.autobots.automanager.entidade.Venda;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VendaMapper {

    // Todos os campos são Long ou String simples — nenhum proxy JPA navegado aqui.
    // itens/servicos devem estar inicializados (JOIN FETCH ou em memória) antes de chamar.
    public VendaResponse toResponse(Venda venda) {
        VendaResponse response = new VendaResponse();
        response.setId(venda.getId());
        response.setCadastro(venda.getCadastro());
        response.setIdentificacao(venda.getIdentificacao());
        response.setStatus(venda.getStatus());
        response.setValorTotal(venda.getValorTotal());
        response.setClienteId(venda.getClienteId());
        response.setClienteNome(venda.getClienteNomeSnapshot());
        response.setFuncionarioId(venda.getFuncionarioId());
        response.setFuncionarioNome(venda.getFuncionarioNomeSnapshot());
        response.setVeiculoId(venda.getVeiculoId());
        response.setVeiculoPlaca(venda.getVeiculoPlacaSnapshot());
        response.setEmpresaId(venda.getEmpresaId());
        response.setItens(mapItens(venda.getItens()));
        response.setServicos(mapServicos(venda.getServicos()));
        return response;
    }

    private List<ItemVendaResponse> mapItens(List<ItemVenda> itens) {
        if (itens == null) return List.of();
        return itens.stream().map(item -> {
            ItemVendaResponse r = new ItemVendaResponse();
            r.setMercadoriaId(item.getMercadoriaId());
            r.setMercadoriaNome(item.getMercadoriaNomeSnapshot());
            r.setPrecoUnitario(item.getPrecoUnitarioSnapshot());
            r.setQuantidade(item.getQuantidade());
            r.setSubtotal(item.getSubtotal());
            return r;
        }).collect(Collectors.toList());
    }

    private List<ItemServicoResponse> mapServicos(List<ItemServico> servicos) {
        if (servicos == null) return List.of();
        return servicos.stream().map(item -> {
            ItemServicoResponse r = new ItemServicoResponse();
            r.setServicoId(item.getServicoId());
            r.setServicoNome(item.getServicoNomeSnapshot());
            r.setPreco(item.getPrecoSnapshot());
            return r;
        }).collect(Collectors.toList());
    }
}
