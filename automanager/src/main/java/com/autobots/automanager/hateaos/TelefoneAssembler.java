package com.autobots.automanager.hateaos;

import com.autobots.automanager.controle.TelefoneController;
import com.autobots.automanager.dto.resposta.TelefoneResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TelefoneAssembler {

    public void addDetailLinks(EntityModel<TelefoneResponse> model, Long id) {
        model.add(linkTo(methodOn(TelefoneController.class).buscar(id)).withSelfRel());
        model.add(linkTo(methodOn(TelefoneController.class).listar()).withRel("todos"));
        model.add(linkTo(methodOn(TelefoneController.class).atualizar(id, null)).withRel("put:atualizar"));
        model.add(linkTo(methodOn(TelefoneController.class).remover(id)).withRel("delete:remover"));
    }

    public void addCollectionLinks(CollectionModel<?> model) {
        model.add(linkTo(methodOn(TelefoneController.class).listar()).withSelfRel());
        model.add(linkTo(TelefoneController.class).withRel("post:criar"));
    }
}
