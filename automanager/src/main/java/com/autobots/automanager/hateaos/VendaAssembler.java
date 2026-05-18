package com.autobots.automanager.hateaos;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.autobots.automanager.controle.VendaController;
import com.autobots.automanager.controle.UsuarioController;
import com.autobots.automanager.controle.VeiculoController;
import com.autobots.automanager.dto.resposta.VendaResponse;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class VendaAssembler implements RepresentationModelAssembler<VendaResponse, VendaResponse> {

    @Override
    public VendaResponse toModel(VendaResponse response) {
        response.add(linkTo(methodOn(VendaController.class)
                .obterVenda(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(VendaController.class)
                .listarVendas()).withRel("vendas"));
        response.add(linkTo(methodOn(VendaController.class)
                .atualizarVenda(response.getId(), null)).withRel("atualizar"));
        response.add(linkTo(methodOn(VendaController.class)
                .excluirVenda(response.getId())).withRel("excluir"));

        if (response.getClienteId() != null) {
            response.add(linkTo(methodOn(UsuarioController.class)
                    .buscar(response.getClienteId())).withRel("cliente"));
        }
        if (response.getVeiculoId() != null) {
            response.add(linkTo(methodOn(VeiculoController.class)
                    .obterVeiculo(response.getVeiculoId())).withRel("veiculo"));
        }
        return response;
    }
}