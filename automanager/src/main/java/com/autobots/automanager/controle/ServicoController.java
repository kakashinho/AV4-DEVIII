package com.autobots.automanager.controle;

import java.net.URI;
import java.util.List;
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

import com.autobots.automanager.dto.requisicao.ServicoRequest;
import com.autobots.automanager.dto.resposta.ServicoResponse;
import com.autobots.automanager.entidade.Servico;
import com.autobots.automanager.hateaos.ServicoAssembler;
import com.autobots.automanager.mapeador.ServicoMapper;
import com.autobots.automanager.servico.ServicoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    @Autowired
    private ServicoService service;

    @Autowired
    private ServicoMapper mapper;

    @Autowired
    private ServicoAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ServicoResponse>>> listar() {
        List<EntityModel<ServicoResponse>> models = service.listarTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<ServicoResponse>> collection = CollectionModel.of(models);
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ServicoResponse>> buscar(@PathVariable Long id) {
        ServicoResponse servico = service.buscarPorId(id);
        EntityModel<ServicoResponse> model = assembler.toModel(servico);
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<EntityModel<ServicoResponse>> criar(
            @Valid @RequestBody ServicoRequest request) {
        ServicoResponse salvo = service.cadastrar(request);
        EntityModel<ServicoResponse> model = assembler.toModel(salvo);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ServicoResponse>> atualizar(
            @PathVariable Long id, @Valid @RequestBody ServicoRequest request) {
        ServicoResponse salvo = service.atualizar(id, request);
        EntityModel<ServicoResponse> model = assembler.toModel(salvo);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}