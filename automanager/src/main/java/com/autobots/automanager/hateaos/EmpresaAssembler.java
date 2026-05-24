package com.autobots.automanager.hateaos;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controle.EmpresaController;
import com.autobots.automanager.controle.MercadoriaController;
import com.autobots.automanager.controle.ServicoController;
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

        model.add(linkTo(methodOn(EmpresaController.class).removerEndereco(id)).withRel("remover-endereco"));
        model.add(linkTo(methodOn(EmpresaController.class).associarTelefone(id, null)).withRel("associar-telefone"));
        model.add(linkTo(methodOn(EmpresaController.class).desassociarTelefone(id, null)).withRel("desassociar-telefone"));
        model.add(linkTo(methodOn(EmpresaController.class).listarUsuarios(id, null)).withRel("usuarios"));
        model.add(linkTo(EmpresaController.class).slash(id).slash("usuarios").withRel("associar-usuario"));

        model.add(linkTo(methodOn(EmpresaController.class).listarMercadorias(id)).withRel("mercadorias"));
        model.add(linkTo(methodOn(MercadoriaController.class).criar(null)).withRel("criar-mercadoria"));

        model.add(linkTo(methodOn(EmpresaController.class).listarServicos(id)).withRel("servicos"));
        model.add(linkTo(methodOn(ServicoController.class).criar(null)).withRel("criar-servico"));

        model.add(linkTo(methodOn(EmpresaController.class).listarVendasDaEmpresa(id)).withRel("vendas"));
        model.add(linkTo(methodOn(EmpresaController.class).criarVendaDaEmpresa(id, null, null)).withRel("criar-venda"));
    }

    public void addCollectionLinks(CollectionModel<?> model) {
        model.add(linkTo(methodOn(EmpresaController.class).listar()).withSelfRel());
        model.add(linkTo(EmpresaController.class).withRel("criar"));
    }

    public void addUsuariosLinks(CollectionModel<?> model, Long empresaId) {
        model.add(linkTo(methodOn(EmpresaController.class).listarUsuarios(empresaId, null)).withSelfRel());
        model.add(linkTo(methodOn(EmpresaController.class).buscar(empresaId)).withRel("empresa"));
    }

    public void addMercadoriasLinks(CollectionModel<?> model, Long empresaId) {
        model.add(linkTo(methodOn(EmpresaController.class).listarMercadorias(empresaId)).withSelfRel());
        model.add(linkTo(methodOn(EmpresaController.class).buscar(empresaId)).withRel("empresa"));
        model.add(linkTo(methodOn(MercadoriaController.class).criar(null)).withRel("criar-mercadoria"));
    }

    public void addServicosLinks(CollectionModel<?> model, Long empresaId) {
        model.add(linkTo(methodOn(EmpresaController.class).listarServicos(empresaId)).withSelfRel());
        model.add(linkTo(methodOn(EmpresaController.class).buscar(empresaId)).withRel("empresa"));
        model.add(linkTo(methodOn(ServicoController.class).criar(null)).withRel("criar-servico"));
    }

    public void addVendasLinks(CollectionModel<?> model, Long empresaId) {
        model.add(linkTo(methodOn(EmpresaController.class).listarVendasDaEmpresa(empresaId)).withSelfRel());
        model.add(linkTo(methodOn(EmpresaController.class).buscar(empresaId)).withRel("empresa"));
        model.add(linkTo(methodOn(EmpresaController.class).criarVendaDaEmpresa(empresaId, null, null)).withRel("criar-venda"));
    }
}
