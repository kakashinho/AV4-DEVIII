package com.autobots.automanager.hateaos;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controle.MercadoriaController;
import com.autobots.automanager.dto.resposta.MercadoriaResponse;

@Component
public class MercadoriaAssembler
        implements RepresentationModelAssembler<MercadoriaResponse, EntityModel<MercadoriaResponse>> {

    @Override
    public EntityModel<MercadoriaResponse> toModel(MercadoriaResponse mercadoria) {

        EntityModel<MercadoriaResponse> model = EntityModel.of(mercadoria);

        model.add(linkTo(methodOn(MercadoriaController.class)
                .buscar(mercadoria.getId())).withSelfRel());

        model.add(linkTo(methodOn(MercadoriaController.class)
                .listar()).withRel("todas"));

        return model;
    }

    @Override
    public CollectionModel<EntityModel<MercadoriaResponse>> toCollectionModel(
            Iterable<? extends MercadoriaResponse> entities) {

        CollectionModel<EntityModel<MercadoriaResponse>> collection =
                RepresentationModelAssembler.super.toCollectionModel(entities);

        collection.add(linkTo(methodOn(MercadoriaController.class)
                .listar()).withSelfRel());

        return collection;
    }
}