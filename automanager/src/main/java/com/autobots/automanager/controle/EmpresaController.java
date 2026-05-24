package com.autobots.automanager.controle;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.autobots.automanager.dto.EmpresaResumo;
import com.autobots.automanager.dto.requisicao.EmpresaRequest;
import com.autobots.automanager.dto.requisicao.EmpresaUpdateRequest;
import com.autobots.automanager.dto.requisicao.MercadoriaRequest;
import com.autobots.automanager.dto.requisicao.ServicoRequest;
import com.autobots.automanager.dto.requisicao.VendaRequest;
import com.autobots.automanager.dto.resposta.EmpresaResponse;
import com.autobots.automanager.dto.resposta.MercadoriaResponse;
import com.autobots.automanager.dto.resposta.ServicoResponse;
import com.autobots.automanager.dto.resposta.UsuarioReferencia;
import com.autobots.automanager.dto.resposta.VendaResponse;
import com.autobots.automanager.enumeracao.PerfilUsuario;
import com.autobots.automanager.hateaos.EmpresaAssembler;
import com.autobots.automanager.hateaos.MercadoriaAssembler;
import com.autobots.automanager.hateaos.ServicoAssembler;
import com.autobots.automanager.hateaos.VendaAssembler;
import com.autobots.automanager.servico.EmpresaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired private EmpresaService empresaService;
    @Autowired private EmpresaAssembler empresaAssembler;
    @Autowired private MercadoriaAssembler mercadoriaAssembler;
    @Autowired private ServicoAssembler servicoAssembler;
    @Autowired private VendaAssembler vendaAssembler;

    //  CRUD principal 

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<EmpresaResumo>>> listar() {
        List<EntityModel<EmpresaResumo>> models = empresaService.listarTodas().stream()
                .map(resumo -> {
                    EntityModel<EmpresaResumo> model = EntityModel.of(resumo);
                    empresaAssembler.addResumoLinks(model, resumo.getId());
                    return model;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<EmpresaResumo>> collection = CollectionModel.of(models);
        empresaAssembler.addCollectionLinks(collection);
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<EmpresaResponse>> buscar(@PathVariable Long id) {
        EmpresaResponse response = empresaService.buscarPorId(id);
        EntityModel<EmpresaResponse> model = EntityModel.of(response);
        empresaAssembler.addDetailLinks(model, id);
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<EntityModel<EmpresaResponse>> criar(
            @Valid @RequestBody EmpresaRequest request) {
        EmpresaResponse response = empresaService.cadastrar(request);
        EntityModel<EmpresaResponse> model = EntityModel.of(response);
        empresaAssembler.addDetailLinks(model, response.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<EmpresaResponse>> atualizar(
            @PathVariable Long id, @Valid @RequestBody EmpresaUpdateRequest request) {
        EmpresaResponse response = empresaService.atualizar(id, request);
        EntityModel<EmpresaResponse> model = EntityModel.of(response);
        empresaAssembler.addDetailLinks(model, id);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        empresaService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/endereco")
    public ResponseEntity<Void> removerEndereco(@PathVariable Long id) {
        empresaService.removerEndereco(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/telefones/{telefoneId}")
    public ResponseEntity<Void> associarTelefone(
            @PathVariable Long id, @PathVariable Long telefoneId) {
        empresaService.associarTelefone(id, telefoneId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/telefones/{telefoneId}")
    public ResponseEntity<Void> desassociarTelefone(
            @PathVariable Long id, @PathVariable Long telefoneId) {
        empresaService.desassociarTelefone(id, telefoneId);
        return ResponseEntity.noContent().build();
    }

    //  Sub-recurso: usuários

    @GetMapping("/{id}/usuarios")
    public ResponseEntity<CollectionModel<EntityModel<UsuarioReferencia>>> listarUsuarios(
            @PathVariable Long id,
            @RequestParam(required = false) PerfilUsuario perfil) {
        Set<UsuarioReferencia> usuarios = empresaService.listarUsuarios(id, perfil);
        List<EntityModel<UsuarioReferencia>> models = usuarios.stream()
                .map(EntityModel::of)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<UsuarioReferencia>> collection = CollectionModel.of(models);
        empresaAssembler.addUsuariosLinks(collection, id);
        return ResponseEntity.ok(collection);
    }

    @PostMapping("/{id}/usuarios/{usuarioId}")
    public ResponseEntity<Void> associarUsuario(
            @PathVariable Long id, @PathVariable Long usuarioId) {
        empresaService.associarUsuario(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/usuarios/{usuarioId}")
    public ResponseEntity<Void> desassociarUsuario(
            @PathVariable Long id, @PathVariable Long usuarioId) {
        empresaService.desassociarUsuario(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    //  Sub-recurso: mercadorias 

    @GetMapping("/{id}/mercadorias")
    public ResponseEntity<CollectionModel<EntityModel<MercadoriaResponse>>> listarMercadorias(
            @PathVariable Long id) {
        List<EntityModel<MercadoriaResponse>> models = empresaService.listarMercadorias(id).stream()
                .map(mercadoriaAssembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<MercadoriaResponse>> collection = CollectionModel.of(models);
        empresaAssembler.addMercadoriasLinks(collection, id);
        return ResponseEntity.ok(collection);
    }

    @PostMapping("/{id}/mercadorias")
    public ResponseEntity<EntityModel<MercadoriaResponse>> criarMercadoria(
            @PathVariable Long id, @Valid @RequestBody MercadoriaRequest request) {
        MercadoriaResponse response = empresaService.criarMercadoria(id, request);
        EntityModel<MercadoriaResponse> model = mercadoriaAssembler.toModel(response);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/mercadorias/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PostMapping("/{id}/mercadorias/{mercadoriaId}")
    public ResponseEntity<EntityModel<MercadoriaResponse>> associarMercadoria(
            @PathVariable Long id, @PathVariable Long mercadoriaId) {
        MercadoriaResponse response = empresaService.associarMercadoria(id, mercadoriaId);
        EntityModel<MercadoriaResponse> model = mercadoriaAssembler.toModel(response);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}/mercadorias/{mercadoriaId}")
    public ResponseEntity<Void> desassociarMercadoria(
            @PathVariable Long id, @PathVariable Long mercadoriaId) {
        empresaService.desassociarMercadoria(id, mercadoriaId);
        return ResponseEntity.noContent().build();
    }

    //  Sub-recurso: serviços 

    @GetMapping("/{id}/servicos")
    public ResponseEntity<CollectionModel<EntityModel<ServicoResponse>>> listarServicos(
            @PathVariable Long id) {
        List<EntityModel<ServicoResponse>> models = empresaService.listarServicos(id).stream()
                .map(servicoAssembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<ServicoResponse>> collection = CollectionModel.of(models);
        empresaAssembler.addServicosLinks(collection, id);
        return ResponseEntity.ok(collection);
    }

    @PostMapping("/{id}/servicos")
    public ResponseEntity<EntityModel<ServicoResponse>> criarServico(
            @PathVariable Long id, @Valid @RequestBody ServicoRequest request) {
        ServicoResponse response = empresaService.criarServico(id, request);
        EntityModel<ServicoResponse> model = servicoAssembler.toModel(response);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/servicos/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PostMapping("/{id}/servicos/{servicoId}")
    public ResponseEntity<Void> associarServico(
            @PathVariable Long id, @PathVariable Long servicoId) {
        empresaService.associarServico(id, servicoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/servicos/{servicoId}")
    public ResponseEntity<Void> desassociarServico(
            @PathVariable Long id, @PathVariable Long servicoId) {
        empresaService.desassociarServico(id, servicoId);
        return ResponseEntity.noContent().build();
    }

    //  Sub-recurso: vendas 
    // @Transactional removido: serviço retorna VendaResponse (mapeamento dentro da transação do serviço)

    @GetMapping("/{empresaId}/vendas")
    public ResponseEntity<CollectionModel<VendaResponse>> listarVendasDaEmpresa(
            @PathVariable Long empresaId) {
        List<VendaResponse> responses = empresaService.listarVendas(empresaId).stream()
                .map(vendaAssembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<VendaResponse> collection = CollectionModel.of(responses);
        empresaAssembler.addVendasLinks(collection, empresaId);
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{empresaId}/vendas/{vendaId}")
    public ResponseEntity<VendaResponse> obterVendaDaEmpresa(
            @PathVariable Long empresaId, @PathVariable Long vendaId) {
        VendaResponse response = vendaAssembler.toModel(empresaService.obterVenda(empresaId, vendaId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{empresaId}/vendas")
    public ResponseEntity<VendaResponse> criarVendaDaEmpresa(
            @PathVariable Long empresaId, @Valid @RequestBody VendaRequest request,
            org.springframework.security.core.Authentication authentication) {
        VendaResponse response = empresaService.criarVenda(empresaId, request, authentication);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/vendas/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(location).body(vendaAssembler.toModel(response));
    }

    @PostMapping("/{empresaId}/vendas/{vendaId}")
    public ResponseEntity<Void> associarVenda(
            @PathVariable Long empresaId, @PathVariable Long vendaId) {
        empresaService.associarVenda(empresaId, vendaId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{empresaId}/vendas/{vendaId}")
    public ResponseEntity<Void> desassociarVenda(
            @PathVariable Long empresaId, @PathVariable Long vendaId) {
        empresaService.desassociarVenda(empresaId, vendaId);
        return ResponseEntity.noContent().build();
    }
}
