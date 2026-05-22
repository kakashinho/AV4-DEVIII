package com.autobots.automanager.controle;

import com.autobots.automanager.dto.requisicao.VeiculoRequest;
import com.autobots.automanager.dto.requisicao.VeiculoUpdateRequest;
import com.autobots.automanager.dto.resposta.VeiculoResponse;
import com.autobots.automanager.entidade.Veiculo;
import com.autobots.automanager.hateaos.VeiculoAssembler;
import com.autobots.automanager.mapeador.VeiculoMapper;
import com.autobots.automanager.servico.VeiculoService;
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
@RequestMapping("/api/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;
    @Autowired
    private VeiculoMapper veiculoMapper;
    @Autowired
    private VeiculoAssembler veiculoAssembler;

    @PostMapping
    public ResponseEntity<VeiculoResponse> criarVeiculo(@Valid @RequestBody VeiculoRequest request) {
        Veiculo salvo = veiculoService.criarVeiculo(request);
        VeiculoResponse response = veiculoAssembler.toModel(veiculoMapper.toResponse(salvo));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<VeiculoResponse>> listarVeiculos() {
        List<VeiculoResponse> responses = veiculoService.listarVeiculos().stream()
                .map(veiculoMapper::toResponse)
                .map(veiculoAssembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<VeiculoResponse> collection = CollectionModel.of(responses);
        collection.add(linkTo(methodOn(VeiculoController.class).listarVeiculos()).withSelfRel());
        collection.add(linkTo(methodOn(VeiculoController.class).criarVeiculo(null)).withRel("criar"));
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponse> obterVeiculo(@PathVariable Long id) {
        Veiculo veiculo = veiculoService.obterVeiculo(id);
        VeiculoResponse response = veiculoMapper.toResponse(veiculo);
        response = veiculoAssembler.toModel(response);
        return ResponseEntity.ok(response);
    }

    // PUT exige entidade completa (tipo, modelo, placa). Proprietário não é
    // alterável aqui — usar PUT /veiculos/{id}/proprietario/{novoUsuarioId}.
    @PutMapping("/{id}")
    public ResponseEntity<VeiculoResponse> atualizarVeiculo(
            @PathVariable Long id, @Valid @RequestBody VeiculoUpdateRequest request) {
        Veiculo veiculoAtualizado = new Veiculo();
        veiculoAtualizado.setTipo(request.getTipo());
        veiculoAtualizado.setModelo(request.getModelo());
        veiculoAtualizado.setPlaca(request.getPlaca());
        Veiculo veiculo = veiculoService.atualizarVeiculo(id, veiculoAtualizado);
        VeiculoResponse response = veiculoMapper.toResponse(veiculo);
        response = veiculoAssembler.toModel(response);
        return ResponseEntity.ok(response);
    }

    // Transferência explícita de propriedade.
    @PutMapping("/{id}/proprietario/{novoUsuarioId}")
    public ResponseEntity<VeiculoResponse> transferirProprietario(
            @PathVariable Long id, @PathVariable Long novoUsuarioId) {
        Veiculo veiculo = veiculoService.transferirProprietario(id, novoUsuarioId);
        VeiculoResponse response = veiculoMapper.toResponse(veiculo);
        response = veiculoAssembler.toModel(response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/proprietario")
    public ResponseEntity<Void> removerProprietario(@PathVariable Long id) {
        veiculoService.removerProprietario(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirVeiculo(@PathVariable Long id) {
        veiculoService.excluirVeiculo(id);
        return ResponseEntity.noContent().build();
    }
}
