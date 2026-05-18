package com.autobots.automanager.hateaos;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.autobots.automanager.controle.UsuarioController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAssembler {

    public void addResumoLinks(EntityModel<?> model, Long id) {
        model.add(linkTo(methodOn(UsuarioController.class).buscar(id)).withRel("detalhe"));
        model.add(linkTo(methodOn(UsuarioController.class).listar()).withRel("todos"));
    }

    public void addDetailLinks(EntityModel<?> model, Long id) {
        // self — GET /api/usuarios/{id}
        model.add(linkTo(methodOn(UsuarioController.class).buscar(id)).withSelfRel());
        model.add(linkTo(methodOn(UsuarioController.class).listar()).withRel("todos"));

        // PUT — atualiza apenas nome/nomeSocial/perfis/endereço (sem coleções)
        model.add(linkTo(methodOn(UsuarioController.class).atualizar(id, null)).withRel("put:atualizar"));

        // DELETE — remove o usuário
        model.add(linkTo(methodOn(UsuarioController.class).remover(id)).withRel("delete:remover"));

        // Sub-recursos — indica endpoint de criação (POST)
        model.add(linkTo(UsuarioController.class).slash(id).slash("telefones").withRel("post:adicionar-telefone"));
        model.add(linkTo(UsuarioController.class).slash(id).slash("emails").withRel("post:adicionar-email"));
        model.add(linkTo(UsuarioController.class).slash(id).slash("documentos").withRel("post:adicionar-documento"));
        model.add(linkTo(UsuarioController.class).slash(id).slash("credenciais").withRel("post:adicionar-credencial"));
    }

    public void addCollectionLinks(CollectionModel<?> model) {
        model.add(linkTo(methodOn(UsuarioController.class).listar()).withSelfRel());
        model.add(linkTo(UsuarioController.class).withRel("post:criar"));
    }

    /**
     * Links para resposta de sub-recurso recém-criado (telefone, email, documento, credencial).
     * <p>
     * "usuario" → GET /api/usuarios/{id} (rota real com GET, usável para navegação)
     * "post:adicionar" → endpoint de adição (POST)
     * "delete:remover" → endpoint de remoção (DELETE /{tipo}/{subId})
     * <p>
     * O self não é adicionado porque não há GET individual para sub-recursos neste modelo.
     */
    public void addSubResourceLinks(EntityModel<?> model, Long usuarioId, String tipo) {
        model.add(linkTo(methodOn(UsuarioController.class).buscar(usuarioId)).withRel("usuario"));
        model.add(linkTo(UsuarioController.class).slash(usuarioId).slash(tipo).withRel("post:adicionar"));
        model.add(linkTo(UsuarioController.class).slash(usuarioId).slash(tipo).slash("{id}").withRel("delete:remover"));
    }
}
