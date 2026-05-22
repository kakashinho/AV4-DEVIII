package com.autobots.automanager.controle;

import com.autobots.automanager.dto.requisicao.VendaRequest;
import com.autobots.automanager.dto.resposta.VendaResponse;
import com.autobots.automanager.entidade.Venda;
import com.autobots.automanager.hateaos.VendaAssembler;
import com.autobots.automanager.mapeador.VendaMapper;
import com.autobots.automanager.servico.VendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    @Autowired private VendaService vendaService;
    @Autowired private VendaMapper vendaMapper;
    @Autowired private VendaAssembler vendaAssembler;

    @PostMapping
    public ResponseEntity<VendaResponse> criarVenda(@Valid @RequestBody VendaRequest request) {
        Venda salva = vendaService.criarVenda(request);
        VendaResponse response = vendaAssembler.toModel(vendaMapper.toResponse(salva));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // @Transactional removido: itens carregados via JOIN FETCH no serviço (findAllComItens)
    @GetMapping
    public ResponseEntity<CollectionModel<VendaResponse>> listarVendas() {
        List<VendaResponse> responses = vendaService.listarVendas().stream()
                .map(vendaMapper::toResponse)
                .map(vendaAssembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<VendaResponse> collection = CollectionModel.of(responses);
        collection.add(linkTo(methodOn(VendaController.class).listarVendas()).withSelfRel());
        collection.add(linkTo(methodOn(VendaController.class).criarVenda(null)).withRel("criar"));
        return ResponseEntity.ok(collection);
    }

    // @Transactional removido: itens carregados via JOIN FETCH no serviço (findByIdComItens)
    @GetMapping("/{id}")
    public ResponseEntity<VendaResponse> obterVenda(@PathVariable Long id) {
        Venda venda = vendaService.obterVenda(id);
        VendaResponse response = vendaAssembler.toModel(vendaMapper.toResponse(venda));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendaResponse> atualizarVenda(
            @PathVariable Long id, @Valid @RequestBody VendaRequest request) {
        Venda venda = vendaService.atualizarVenda(id, request);
        VendaResponse response = vendaAssembler.toModel(vendaMapper.toResponse(venda));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirVenda(@PathVariable Long id) {
        vendaService.excluirVenda(id);
        return ResponseEntity.noContent().build();
    }
}
