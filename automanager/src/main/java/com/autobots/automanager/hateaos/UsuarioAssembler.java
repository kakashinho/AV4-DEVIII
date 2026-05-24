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
        model.add(linkTo(methodOn(UsuarioController.class).buscar(id, null)).withRel("detalhe"));
        model.add(linkTo(methodOn(UsuarioController.class).listar()).withRel("todos"));
    }

    public void addDetailLinks(EntityModel<?> model, Long id) {
        // self — GET /api/usuarios/{id}
        model.add(linkTo(methodOn(UsuarioController.class).buscar(id, null)).withSelfRel());
        model.add(linkTo(methodOn(UsuarioController.class).listar()).withRel("todos"));

        // PUT — atualiza nome/nomeSocial/perfis/endereço (sem coleções). Perfis também são gerenciados aqui.
        model.add(linkTo(methodOn(UsuarioController.class).atualizar(id, null, null)).withRel("put:atualizar"));
        model.add(linkTo(methodOn(UsuarioController.class).atualizar(id, null, null)).withRel("put:gerenciar-perfis"));

        // DELETE — remove o usuário
        model.add(linkTo(methodOn(UsuarioController.class).remover(id, null)).withRel("delete:remover"));

        // Telefones
        model.add(linkTo(UsuarioController.class).slash(id).slash("telefones").withRel("post:adicionar-telefone"));
        model.add(linkTo(UsuarioController.class).slash(id).slash("telefones").slash("{telefoneId}").withRel("put:atualizar-telefone"));
        model.add(linkTo(UsuarioController.class).slash(id).slash("telefones").slash("{telefoneId}").withRel("delete:remover-telefone"));

        // Emails
        model.add(linkTo(UsuarioController.class).slash(id).slash("emails").withRel("post:adicionar-email"));
        model.add(linkTo(UsuarioController.class).slash(id).slash("emails").slash("{emailId}").withRel("put:atualizar-email"));
        model.add(linkTo(UsuarioController.class).slash(id).slash("emails").slash("{emailId}").withRel("delete:remover-email"));

        // Documentos
        model.add(linkTo(UsuarioController.class).slash(id).slash("documentos").withRel("post:adicionar-documento"));
        model.add(linkTo(UsuarioController.class).slash(id).slash("documentos").slash("{documentoId}").withRel("put:atualizar-documento"));
        model.add(linkTo(UsuarioController.class).slash(id).slash("documentos").slash("{documentoId}").withRel("delete:remover-documento"));

        // Credenciais
        model.add(linkTo(UsuarioController.class).slash(id).slash("credenciais").withRel("post:adicionar-credencial"));
        model.add(linkTo(UsuarioController.class).slash(id).slash("credenciais").slash("{credencialId}").withRel("put:atualizar-credencial"));
        model.add(linkTo(UsuarioController.class).slash(id).slash("credenciais").slash("{credencialId}").withRel("delete:remover-credencial"));

        // Veículos
        model.add(linkTo(methodOn(UsuarioController.class).listarVeiculos(id, null)).withRel("get:listar-veiculos"));
        model.add(linkTo(UsuarioController.class).slash(id).slash("veiculos").slash("{veiculoId}").withRel("post:associar-veiculo"));
        model.add(linkTo(UsuarioController.class).slash(id).slash("veiculos").slash("{veiculoId}").withRel("delete:remover-veiculo"));

        // Endereço
        model.add(linkTo(methodOn(UsuarioController.class).buscarEndereco(id, null)).withRel("get:endereco"));
        model.add(linkTo(methodOn(UsuarioController.class).definirEndereco(id, null, null)).withRel("post:definir-endereco"));
        model.add(linkTo(methodOn(UsuarioController.class).removerEndereco(id)).withRel("delete:remover-endereco"));

        // Vendas
        model.add(linkTo(methodOn(UsuarioController.class).listarVendasDoUsuario(id, null)).withRel("get:listar-vendas"));
        model.add(linkTo(UsuarioController.class).slash(id).slash("vendas").slash("{vendaId}").withRel("post:associar-venda"));
        model.add(linkTo(UsuarioController.class).slash(id).slash("vendas").slash("{vendaId}").withRel("delete:remover-venda"));

        // Coleções de sub-recursos (listagem)
        model.add(linkTo(methodOn(UsuarioController.class).listarTelefones(id, null)).withRel("get:listar-telefones"));
        model.add(linkTo(methodOn(UsuarioController.class).listarEmails(id, null)).withRel("get:listar-emails"));
        model.add(linkTo(methodOn(UsuarioController.class).listarDocumentos(id, null)).withRel("get:listar-documentos"));
        model.add(linkTo(methodOn(UsuarioController.class).listarCredenciais(id)).withRel("get:listar-credenciais"));
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
        model.add(linkTo(methodOn(UsuarioController.class).buscar(usuarioId, null)).withRel("usuario"));
        model.add(linkTo(UsuarioController.class).slash(usuarioId).slash(tipo).withRel("post:adicionar"));
        model.add(linkTo(UsuarioController.class).slash(usuarioId).slash(tipo).slash("{subId}").withRel("delete:remover"));
    }
}
