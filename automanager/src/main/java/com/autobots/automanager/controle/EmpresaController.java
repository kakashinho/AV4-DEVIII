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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.autobots.automanager.dto.EmpresaResumo;
import com.autobots.automanager.dto.requisicao.EmpresaRequest;
import com.autobots.automanager.dto.resposta.EmpresaResponse;
import com.autobots.automanager.dto.resposta.UsuarioReferencia;
import com.autobots.automanager.dto.resposta.VeiculoResponse;
import com.autobots.automanager.dto.resposta.VendaResponse;
import com.autobots.automanager.entidade.Empresa;
import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.entidade.Veiculo;
import com.autobots.automanager.entidade.Venda;
import com.autobots.automanager.hateaos.EmpresaAssembler;
import com.autobots.automanager.hateaos.VeiculoAssembler;
import com.autobots.automanager.hateaos.VendaAssembler;
import com.autobots.automanager.mapeador.VeiculoMapper;
import com.autobots.automanager.mapeador.VendaMapper;
import com.autobots.automanager.repositorio.RepositorioUsuario;
import com.autobots.automanager.repositorio.RepositorioVeiculo;
import com.autobots.automanager.repositorio.RepositorioVenda;
import com.autobots.automanager.servico.EmpresaService;

import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private EmpresaAssembler empresaAssembler;

    @Autowired
    private RepositorioUsuario usuarioRepo;

    @Autowired
    private RepositorioVeiculo veiculoRepo;

    @Autowired
    private RepositorioVenda vendaRepo;

    @Autowired
    private VeiculoMapper veiculoMapper;

    @Autowired
    private VeiculoAssembler veiculoAssembler;

    @Autowired
    private VendaMapper vendaMapper;

    @Autowired
    private VendaAssembler vendaAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<EmpresaResumo>>> listar() {
        List<EmpresaResumo> resumos = empresaService.listarTodas();
        List<EntityModel<EmpresaResumo>> models = resumos.stream()
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
            @PathVariable Long id, @Valid @RequestBody EmpresaRequest request) {
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

    @GetMapping("/{id}/usuarios")
    public ResponseEntity<CollectionModel<EntityModel<UsuarioReferencia>>> listarUsuarios(
            @PathVariable Long id) {
        Set<UsuarioReferencia> usuarios = empresaService.listarUsuarios(id);
        List<EntityModel<UsuarioReferencia>> models = usuarios.stream()
                .map(ref -> EntityModel.of(ref))
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

    @GetMapping("/{empresaId}/vendas")
    @Transactional(readOnly = true)
    public ResponseEntity<List<VendaResponse>> listarVendasDaEmpresa(
            @PathVariable Long empresaId) {
        Empresa empresa = empresaService.obterEmpresa(empresaId);
        Set<Usuario> usuarios = empresa.getUsuarios();

        List<Venda> vendas = vendaRepo.findAll().stream()
                .filter(v -> usuarios.contains(v.getCliente()) ||
                             usuarios.contains(v.getFuncionario()))
                .collect(Collectors.toList());

        List<VendaResponse> responses = vendas.stream()
                .map(vendaMapper::toResponse)
                .map(vendaAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}