package com.autobots.automanager.hateaos;

import com.autobots.automanager.controle.DocumentoController;
import com.autobots.automanager.dto.resposta.DocumentoResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DocumentoAssembler {

    public void addDetailLinks(EntityModel<DocumentoResponse> model, Long id) {
        model.add(linkTo(methodOn(DocumentoController.class).buscar(id)).withSelfRel());
        model.add(linkTo(methodOn(DocumentoController.class).listar()).withRel("todos"));
        model.add(linkTo(methodOn(DocumentoController.class).atualizar(id, null)).withRel("put:atualizar"));
        model.add(linkTo(methodOn(DocumentoController.class).remover(id)).withRel("delete:remover"));
    }

    public void addCollectionLinks(CollectionModel<?> model) {
        model.add(linkTo(methodOn(DocumentoController.class).listar()).withSelfRel());
        model.add(linkTo(DocumentoController.class).withRel("post:criar"));
    }
}
