package com.autobots.automanager.mapeador;

import com.autobots.automanager.dto.resposta.VendaResponse;
import com.autobots.automanager.entidade.Mercadoria;
import com.autobots.automanager.entidade.Servico;
import com.autobots.automanager.entidade.Venda;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

// Conversão Entidade → DTO de resposta apenas.
// A construção da entidade a partir do request é responsabilidade do VendaService,
// que precisa validar IDs em conjunto e acumular erros antes de instanciar.
@Component
public class VendaMapper {

    public VendaResponse toResponse(Venda venda) {
        VendaResponse response = new VendaResponse();
        response.setId(venda.getId());
        response.setCadastro(venda.getCadastro());
        response.setIdentificacao(venda.getIdentificacao());

        if (venda.getCliente() != null) {
            response.setClienteId(venda.getCliente().getId());
            response.setClienteNome(venda.getCliente().getNome());
        }
        if (venda.getFuncionario() != null) {
            response.setFuncionarioId(venda.getFuncionario().getId());
            response.setFuncionarioNome(venda.getFuncionario().getNome());
        }
        if (venda.getVeiculo() != null) {
            response.setVeiculoId(venda.getVeiculo().getId());
            response.setVeiculoModelo(venda.getVeiculo().getModelo());
        }
        response.setMercadoriasIds(venda.getMercadorias().stream()
                .map(Mercadoria::getId).collect(Collectors.toSet()));
        response.setServicosIds(venda.getServicos().stream()
                .map(Servico::getId).collect(Collectors.toSet()));
        return response;
    }
}
