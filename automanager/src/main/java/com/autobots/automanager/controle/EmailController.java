package com.autobots.automanager.controle;

import com.autobots.automanager.dto.requisicao.EmailRequest;
import com.autobots.automanager.dto.resposta.EmailResponse;
import com.autobots.automanager.entidade.Email;
import com.autobots.automanager.hateaos.EmailAssembler;
import com.autobots.automanager.mapeador.EmailMapper;
import com.autobots.automanager.servico.EmailService;
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
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService service;
    private final EmailAssembler assembler;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<CollectionModel<EntityModel<EmailResponse>>> listar() {
        List<EntityModel<EmailResponse>> models = service.listarTodos().stream()
                .map(e -> {
                    EntityModel<EmailResponse> model = EntityModel.of(EmailMapper.toResponse(e));
                    assembler.addDetailLinks(model, e.getId());
                    return model;
                })
                .collect(Collectors.toList());
        CollectionModel<EntityModel<EmailResponse>> collection = CollectionModel.of(models);
        assembler.addCollectionLinks(collection);
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<EntityModel<EmailResponse>> buscar(@PathVariable Long id) {
        Email email = service.buscarPorId(id);
        EntityModel<EmailResponse> model = EntityModel.of(EmailMapper.toResponse(email));
        assembler.addDetailLinks(model, id);
        return ResponseEntity.ok(model);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<EntityModel<EmailResponse>> criar(@Valid @RequestBody EmailRequest request) {
        Email email = service.criar(request);
        EntityModel<EmailResponse> model = EntityModel.of(EmailMapper.toResponse(email));
        assembler.addDetailLinks(model, email.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(email.getId()).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<EntityModel<EmailResponse>> atualizar(
            @PathVariable Long id, @Valid @RequestBody EmailRequest request) {
        Email email = service.atualizar(id, request);
        EntityModel<EmailResponse> model = EntityModel.of(EmailMapper.toResponse(email));
        assembler.addDetailLinks(model, id);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
