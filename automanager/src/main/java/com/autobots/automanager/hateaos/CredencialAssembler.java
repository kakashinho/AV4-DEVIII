package com.autobots.automanager.hateaos;

import com.autobots.automanager.controle.CredencialController;
import com.autobots.automanager.dto.resposta.CredencialResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CredencialAssembler {

    public void addDetailLinks(EntityModel<CredencialResponse> model, Long id) {
        model.add(linkTo(methodOn(CredencialController.class).buscar(id)).withSelfRel());
        model.add(linkTo(methodOn(CredencialController.class).listar()).withRel("todas"));
        model.add(linkTo(methodOn(CredencialController.class).atualizar(id, null)).withRel("put:atualizar-status"));
        model.add(linkTo(methodOn(CredencialController.class).remover(id)).withRel("delete:remover"));
    }

    public void addCollectionLinks(CollectionModel<?> model) {
        model.add(linkTo(methodOn(CredencialController.class).listar()).withSelfRel());
        model.add(linkTo(CredencialController.class).withRel("post:criar"));
    }
}
