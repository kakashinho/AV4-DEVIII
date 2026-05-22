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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.autobots.automanager.dto.requisicao.EstoqueRequest;
import com.autobots.automanager.dto.requisicao.MercadoriaRequest;
import com.autobots.automanager.dto.requisicao.MercadoriaUpdateRequest;
import com.autobots.automanager.dto.resposta.MercadoriaResponse;
import com.autobots.automanager.enumeracao.TipoMovimentoEstoque;
import com.autobots.automanager.hateaos.MercadoriaAssembler;
import com.autobots.automanager.servico.MercadoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/mercadorias")
public class MercadoriaController {

    @Autowired private MercadoriaService service;
    @Autowired private MercadoriaAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<MercadoriaResponse>>> listar() {
        List<EntityModel<MercadoriaResponse>> models = service.listarTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<MercadoriaResponse>> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toModel(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<EntityModel<MercadoriaResponse>> criar(
            @Valid @RequestBody MercadoriaRequest request) {
        MercadoriaResponse salva = service.cadastrar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(salva.getId()).toUri();
        return ResponseEntity.created(location).body(assembler.toModel(salva));
    }

    // PUT atualiza apenas dados descritivos (nome, valor, datas).
    // Quantidade de estoque é gerenciada exclusivamente via PATCH /estoque.
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<MercadoriaResponse>> atualizar(
            @PathVariable Long id, @Valid @RequestBody MercadoriaUpdateRequest request) {
        return ResponseEntity.ok(assembler.toModel(service.atualizar(id, request)));
    }

    // Movimentação de estoque dedicada: ENTRADA aumenta, SAIDA diminui.
    @PatchMapping("/{id}/estoque")
    public ResponseEntity<EntityModel<MercadoriaResponse>> ajustarEstoque(
            @PathVariable Long id, @Valid @RequestBody EstoqueRequest request) {
        boolean entrada = request.getTipo() == TipoMovimentoEstoque.ENTRADA;
        MercadoriaResponse response = service.ajustarEstoque(id, request.getQuantidade(), entrada);
        return ResponseEntity.ok(assembler.toModel(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
