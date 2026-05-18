package com.autobots.automanager.controle;

import com.autobots.automanager.dto.requisicao.CredencialRequest;
import com.autobots.automanager.dto.requisicao.CredencialUpdateRequest;
import com.autobots.automanager.dto.resposta.CredencialResponse;
import com.autobots.automanager.entidade.Credencial;
import com.autobots.automanager.hateaos.CredencialAssembler;
import com.autobots.automanager.mapeador.CredencialMapper;
import com.autobots.automanager.servico.CredencialService;
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
@RequestMapping("/api/credenciais")
@RequiredArgsConstructor
public class CredencialController {

    private final CredencialService service;
    private final CredencialAssembler assembler;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<CollectionModel<EntityModel<CredencialResponse>>> listar() {
        List<EntityModel<CredencialResponse>> models = service.listarTodos().stream()
                .map(c -> {
                    EntityModel<CredencialResponse> model = EntityModel.of(CredencialMapper.toResponse(c));
                    assembler.addDetailLinks(model, c.getId());
                    return model;
                })
                .collect(Collectors.toList());
        CollectionModel<EntityModel<CredencialResponse>> collection = CollectionModel.of(models);
        assembler.addCollectionLinks(collection);
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<EntityModel<CredencialResponse>> buscar(@PathVariable Long id) {
        Credencial cred = service.buscarPorId(id);
        EntityModel<CredencialResponse> model = EntityModel.of(CredencialMapper.toResponse(cred));
        assembler.addDetailLinks(model, id);
        return ResponseEntity.ok(model);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<EntityModel<CredencialResponse>> criar(@Valid @RequestBody CredencialRequest request) {
        Credencial cred = service.criar(request);
        EntityModel<CredencialResponse> model = EntityModel.of(CredencialMapper.toResponse(cred));
        assembler.addDetailLinks(model, cred.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(cred.getId()).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<EntityModel<CredencialResponse>> atualizar(
            @PathVariable Long id, @Valid @RequestBody CredencialUpdateRequest request) {
        Credencial cred = service.atualizar(id, request);
        EntityModel<CredencialResponse> model = EntityModel.of(CredencialMapper.toResponse(cred));
        assembler.addDetailLinks(model, id);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
