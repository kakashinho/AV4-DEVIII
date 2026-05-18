package com.autobots.automanager.hateaos;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controle.ServicoController;
import com.autobots.automanager.dto.resposta.ServicoResponse;

@Component
public class ServicoAssembler {

    public EntityModel<ServicoResponse> toModel(ServicoResponse response) {
        EntityModel<ServicoResponse> model = EntityModel.of(response);
        addLinksDetalhe(model, response.getId());
        return model;
    }

    public void addLinksDetalhe(EntityModel<?> model, Long id) {
        model.add(linkTo(methodOn(ServicoController.class).buscar(id)).withSelfRel());
        model.add(linkTo(methodOn(ServicoController.class).listar()).withRel("todas"));
        model.add(linkTo(methodOn(ServicoController.class).atualizar(id, null)).withRel("atualizar"));
        model.add(linkTo(methodOn(ServicoController.class).remover(id)).withRel("remover"));
    }

    public void addLinkColecao(CollectionModel<?> collection) {
        collection.add(linkTo(methodOn(ServicoController.class).listar()).withSelfRel());
    }

    public void addLinkDetalhe(EntityModel<?> model, Long id) {
        model.add(linkTo(methodOn(ServicoController.class).buscar(id)).withSelfRel());
    }
}