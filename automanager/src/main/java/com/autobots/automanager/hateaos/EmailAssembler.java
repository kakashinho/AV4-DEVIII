package com.autobots.automanager.hateaos;

import com.autobots.automanager.controle.EmailController;
import com.autobots.automanager.dto.resposta.EmailResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmailAssembler {

    public void addDetailLinks(EntityModel<EmailResponse> model, Long id) {
        model.add(linkTo(methodOn(EmailController.class).buscar(id)).withSelfRel());
        model.add(linkTo(methodOn(EmailController.class).listar()).withRel("todos"));
        model.add(linkTo(methodOn(EmailController.class).atualizar(id, null)).withRel("put:atualizar"));
        model.add(linkTo(methodOn(EmailController.class).remover(id)).withRel("delete:remover"));
    }

    public void addCollectionLinks(CollectionModel<?> model) {
        model.add(linkTo(methodOn(EmailController.class).listar()).withSelfRel());
        model.add(linkTo(EmailController.class).withRel("post:criar"));
    }
}
