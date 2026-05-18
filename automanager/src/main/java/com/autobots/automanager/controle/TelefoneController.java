package com.autobots.automanager.controle;

import com.autobots.automanager.dto.requisicao.TelefoneRequest;
import com.autobots.automanager.dto.resposta.TelefoneResponse;
import com.autobots.automanager.entidade.Telefone;
import com.autobots.automanager.hateaos.TelefoneAssembler;
import com.autobots.automanager.mapeador.TelefoneMapper;
import com.autobots.automanager.servico.TelefoneService;
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
@RequestMapping("/api/telefones")
@RequiredArgsConstructor
public class TelefoneController {

    private final TelefoneService service;
    private final TelefoneAssembler assembler;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<CollectionModel<EntityModel<TelefoneResponse>>> listar() {
        List<EntityModel<TelefoneResponse>> models = service.listarTodos().stream()
                .map(t -> {
                    EntityModel<TelefoneResponse> model = EntityModel.of(TelefoneMapper.toResponse(t));
                    assembler.addDetailLinks(model, t.getId());
                    return model;
                })
                .collect(Collectors.toList());
        CollectionModel<EntityModel<TelefoneResponse>> collection = CollectionModel.of(models);
        assembler.addCollectionLinks(collection);
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<EntityModel<TelefoneResponse>> buscar(@PathVariable Long id) {
        Telefone telefone = service.buscarPorId(id);
        EntityModel<TelefoneResponse> model = EntityModel.of(TelefoneMapper.toResponse(telefone));
        assembler.addDetailLinks(model, id);
        return ResponseEntity.ok(model);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<EntityModel<TelefoneResponse>> criar(@Valid @RequestBody TelefoneRequest request) {
        Telefone telefone = service.criar(request);
        EntityModel<TelefoneResponse> model = EntityModel.of(TelefoneMapper.toResponse(telefone));
        assembler.addDetailLinks(model, telefone.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(telefone.getId()).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<EntityModel<TelefoneResponse>> atualizar(
            @PathVariable Long id, @Valid @RequestBody TelefoneRequest request) {
        Telefone telefone = service.atualizar(id, request);
        EntityModel<TelefoneResponse> model = EntityModel.of(TelefoneMapper.toResponse(telefone));
        assembler.addDetailLinks(model, id);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
