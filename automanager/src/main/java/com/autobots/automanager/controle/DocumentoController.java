package com.autobots.automanager.controle;

import com.autobots.automanager.dto.requisicao.DocumentoRequest;
import com.autobots.automanager.dto.resposta.DocumentoResponse;
import com.autobots.automanager.entidade.Documento;
import com.autobots.automanager.hateaos.DocumentoAssembler;
import com.autobots.automanager.mapeador.DocumentoMapper;
import com.autobots.automanager.servico.DocumentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documentos")
@RequiredArgsConstructor
public class DocumentoController {

    private final DocumentoService service;
    private final DocumentoAssembler assembler;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<CollectionModel<EntityModel<DocumentoResponse>>> listar() {
        List<EntityModel<DocumentoResponse>> models = service.listarTodos().stream()
                .map(d -> {
                    EntityModel<DocumentoResponse> model = EntityModel.of(DocumentoMapper.toResponse(d));
                    assembler.addDetailLinks(model, d.getId());
                    return model;
                })
                .collect(Collectors.toList());
        CollectionModel<EntityModel<DocumentoResponse>> collection = CollectionModel.of(models);
        assembler.addCollectionLinks(collection);
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<EntityModel<DocumentoResponse>> buscar(@PathVariable Long id) {
        Documento doc = service.buscarPorId(id);
        EntityModel<DocumentoResponse> model = EntityModel.of(DocumentoMapper.toResponse(doc));
        assembler.addDetailLinks(model, id);
        return ResponseEntity.ok(model);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<EntityModel<DocumentoResponse>> criar(@Valid @RequestBody DocumentoRequest request) {
        Documento doc = service.criar(request);
        EntityModel<DocumentoResponse> model = EntityModel.of(DocumentoMapper.toResponse(doc));
        assembler.addDetailLinks(model, doc.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(doc.getId()).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<EntityModel<DocumentoResponse>> atualizar(
            @PathVariable Long id, @Valid @RequestBody DocumentoRequest request) {
        Documento doc = service.atualizar(id, request);
        EntityModel<DocumentoResponse> model = EntityModel.of(DocumentoMapper.toResponse(doc));
        assembler.addDetailLinks(model, id);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
