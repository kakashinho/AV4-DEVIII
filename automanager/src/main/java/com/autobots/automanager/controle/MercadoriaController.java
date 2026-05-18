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

import com.autobots.automanager.dto.requisicao.MercadoriaRequest;
import com.autobots.automanager.dto.resposta.MercadoriaResponse;
import com.autobots.automanager.entidade.Mercadoria;
import com.autobots.automanager.hateaos.MercadoriaAssembler;
import com.autobots.automanager.mapeador.MercadoriaMapper;
import com.autobots.automanager.servico.MercadoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/mercadorias")
public class MercadoriaController {

    @Autowired
    private MercadoriaService service;

    @Autowired
    private MercadoriaMapper mapper;

    @Autowired
    private MercadoriaAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<MercadoriaResponse>>> listar() {
        List<EntityModel<MercadoriaResponse>> models = service.listarTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<MercadoriaResponse>> collection = CollectionModel.of(models);
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<MercadoriaResponse>> buscar(@PathVariable Long id) {
        MercadoriaResponse mercadoria = service.buscarPorId(id);
        EntityModel<MercadoriaResponse> model = assembler.toModel(mercadoria);
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<EntityModel<MercadoriaResponse>> criar(
            @Valid @RequestBody MercadoriaRequest request) {
        MercadoriaResponse salva = service.cadastrar(request);
        EntityModel<MercadoriaResponse> model = assembler.toModel(salva);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(salva.getId()).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<MercadoriaResponse>> atualizar(
            @PathVariable Long id, @Valid @RequestBody MercadoriaRequest request) {
        MercadoriaResponse salva = service.atualizar(id, request);
        EntityModel<MercadoriaResponse> model = assembler.toModel(salva);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}