package com.autobots.automanager.hateaos;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controle.EmpresaController;
import com.autobots.automanager.dto.EmpresaResumo;
import com.autobots.automanager.dto.resposta.EmpresaResponse;

@Component
public class EmpresaAssembler {

    public void addResumoLinks(EntityModel<EmpresaResumo> model, Long id) {
        model.add(linkTo(methodOn(EmpresaController.class).buscar(id)).withRel("detalhe"));
        model.add(linkTo(methodOn(EmpresaController.class).listar()).withRel("todas"));
    }

    public void addDetailLinks(EntityModel<EmpresaResponse> model, Long id) {
        model.add(linkTo(methodOn(EmpresaController.class).buscar(id)).withSelfRel());
        model.add(linkTo(methodOn(EmpresaController.class).listar()).withRel("todas"));
        model.add(linkTo(methodOn(EmpresaController.class).atualizar(id, null)).withRel("editar"));
        model.add(linkTo(methodOn(EmpresaController.class).remover(id)).withRel("remover"));
        model.add(linkTo(methodOn(EmpresaController.class).listarUsuarios(id)).withRel("usuarios"));
        model.add(linkTo(EmpresaController.class).slash(id).slash("usuarios").withRel("associar-usuario"));
    }

    public void addCollectionLinks(CollectionModel<?> model) {
        model.add(linkTo(methodOn(EmpresaController.class).listar()).withSelfRel());
        model.add(linkTo(EmpresaController.class).withRel("criar"));
    }

    public void addUsuariosLinks(CollectionModel<?> model, Long empresaId) {
        model.add(linkTo(methodOn(EmpresaController.class).listarUsuarios(empresaId)).withSelfRel());
        model.add(linkTo(methodOn(EmpresaController.class).buscar(empresaId)).withRel("empresa"));
    }
}