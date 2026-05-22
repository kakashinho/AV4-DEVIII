package com.autobots.automanager.hateaos;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controle.EnderecoController;
import com.autobots.automanager.dto.resposta.EnderecoResponse;

@Component
public class EnderecoAssembler {

    public EnderecoResponse toModel(EnderecoResponse response) {
        Long id = response.getId();
        response.add(linkTo(methodOn(EnderecoController.class).buscar(id)).withSelfRel());
        response.add(linkTo(methodOn(EnderecoController.class).listar()).withRel("todos"));
        response.add(linkTo(methodOn(EnderecoController.class).atualizar(id, null)).withRel("atualizar"));
        response.add(linkTo(methodOn(EnderecoController.class).remover(id)).withRel("remover"));
        return response;
    }

    public void addCollectionLinks(CollectionModel<?> model) {
        model.add(linkTo(methodOn(EnderecoController.class).listar()).withSelfRel());
        model.add(linkTo(EnderecoController.class).withRel("criar"));
    }
}
