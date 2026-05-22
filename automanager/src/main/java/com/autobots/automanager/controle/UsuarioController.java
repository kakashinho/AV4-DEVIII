package com.autobots.automanager.controle;

import com.autobots.automanager.dto.requisicao.AssociacaoCredencialRequest;
import com.autobots.automanager.dto.requisicao.AssociacaoDocumentoRequest;
import com.autobots.automanager.dto.requisicao.AssociacaoEmailRequest;
import com.autobots.automanager.dto.requisicao.AssociacaoTelefoneRequest;
import com.autobots.automanager.dto.requisicao.CredencialUpdateRequest;
import com.autobots.automanager.dto.requisicao.DocumentoRequest;
import com.autobots.automanager.dto.requisicao.EmailRequest;
import com.autobots.automanager.dto.requisicao.TelefoneRequest;
import com.autobots.automanager.dto.requisicao.UsuarioRequest;
import com.autobots.automanager.dto.requisicao.UsuarioUpdateRequest;
import com.autobots.automanager.dto.resposta.CredencialResponse;
import com.autobots.automanager.dto.resposta.DocumentoResponse;
import com.autobots.automanager.dto.resposta.EmailResponse;
import com.autobots.automanager.dto.resposta.TelefoneResponse;
import com.autobots.automanager.dto.resposta.UsuarioResponse;
import com.autobots.automanager.dto.resposta.UsuarioResumo;
import com.autobots.automanager.dto.resposta.VeiculoResponse;
import com.autobots.automanager.entidade.Credencial;
import com.autobots.automanager.entidade.Documento;
import com.autobots.automanager.entidade.Email;
import com.autobots.automanager.entidade.Telefone;
import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.entidade.Veiculo;
import com.autobots.automanager.hateaos.UsuarioAssembler;
import com.autobots.automanager.hateaos.VeiculoAssembler;
import com.autobots.automanager.mapeador.CredencialMapper;
import com.autobots.automanager.mapeador.DocumentoMapper;
import com.autobots.automanager.mapeador.EmailMapper;
import com.autobots.automanager.mapeador.TelefoneMapper;
import com.autobots.automanager.mapeador.UsuarioMapper;
import com.autobots.automanager.mapeador.VeiculoMapper;
import com.autobots.automanager.servico.CredencialService;
import com.autobots.automanager.servico.DocumentoService;
import com.autobots.automanager.servico.EmailService;
import com.autobots.automanager.servico.TelefoneService;
import com.autobots.automanager.servico.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;
    private final UsuarioAssembler assembler;

    @Autowired private VeiculoMapper veiculoMapper;
    @Autowired private VeiculoAssembler veiculoAssembler;
    @Autowired private TelefoneService telefoneService;
    @Autowired private EmailService emailService;
    @Autowired private DocumentoService documentoService;
    @Autowired private CredencialService credencialService;

    // ─── CRUD de usuário ──────────────────────────────────────────────────────

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<CollectionModel<EntityModel<UsuarioResumo>>> listar() {
        List<Usuario> usuarios = service.listarTodos();
        List<EntityModel<UsuarioResumo>> resumos = usuarios.stream()
                .map(u -> {
                    UsuarioResumo resumo = mapper.paraResumo(u);
                    EntityModel<UsuarioResumo> model = EntityModel.of(resumo);
                    assembler.addResumoLinks(model, u.getId());
                    return model;
                })
                .collect(Collectors.toList());
        CollectionModel<EntityModel<UsuarioResumo>> collection = CollectionModel.of(resumos);
        assembler.addCollectionLinks(collection);
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<EntityModel<UsuarioResponse>> buscar(@PathVariable Long id) {
        Usuario usuario = service.buscarPorId(id);
        UsuarioResponse response = mapper.paraResponse(usuario);
        EntityModel<UsuarioResponse> model = EntityModel.of(response);
        assembler.addDetailLinks(model, id);
        return ResponseEntity.ok(model);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<EntityModel<UsuarioResponse>> criar(@Valid @RequestBody UsuarioRequest request) {
        Usuario usuario = service.cadastrar(request);
        UsuarioResponse response = mapper.paraResponse(usuario);
        EntityModel<UsuarioResponse> model = EntityModel.of(response);
        assembler.addDetailLinks(model, usuario.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<EntityModel<UsuarioResponse>> atualizar(
            @PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequest request) {
        Usuario usuario = service.atualizar(id, request);
        UsuarioResponse response = mapper.paraResponse(usuario);
        EntityModel<UsuarioResponse> model = EntityModel.of(response);
        assembler.addDetailLinks(model, id);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Associações: telefones ───────────────────────────────────────────────

    @PostMapping("/{id}/telefones")
    public ResponseEntity<EntityModel<TelefoneResponse>> associarTelefone(
            @PathVariable Long id, @Valid @RequestBody AssociacaoTelefoneRequest request) {
        Telefone tel = service.associarTelefone(id, request.getTelefoneId());
        TelefoneResponse resp = TelefoneMapper.toResponse(tel);
        EntityModel<TelefoneResponse> model = EntityModel.of(resp);
        assembler.addSubResourceLinks(model, id, "telefones");
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/telefones/{id}").buildAndExpand(tel.getId()).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}/telefones/{telefoneId}")
    public ResponseEntity<EntityModel<TelefoneResponse>> atualizarTelefone(
            @PathVariable Long id, @PathVariable Long telefoneId,
            @Valid @RequestBody TelefoneRequest request) {
        service.buscarPorId(id);
        Telefone tel = telefoneService.atualizar(telefoneId, request);
        EntityModel<TelefoneResponse> model = EntityModel.of(TelefoneMapper.toResponse(tel));
        assembler.addSubResourceLinks(model, id, "telefones");
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}/telefones/{telefoneId}")
    public ResponseEntity<Void> desassociarTelefone(
            @PathVariable Long id, @PathVariable Long telefoneId) {
        service.desassociarTelefone(id, telefoneId);
        return ResponseEntity.noContent().build();
    }

    // ─── Associações: emails ──────────────────────────────────────────────────

    @PostMapping("/{id}/emails")
    public ResponseEntity<EntityModel<EmailResponse>> associarEmail(
            @PathVariable Long id, @Valid @RequestBody AssociacaoEmailRequest request) {
        Email email = service.associarEmail(id, request.getEmailId());
        EmailResponse resp = EmailMapper.toResponse(email);
        EntityModel<EmailResponse> model = EntityModel.of(resp);
        assembler.addSubResourceLinks(model, id, "emails");
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/emails/{id}").buildAndExpand(email.getId()).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}/emails/{emailId}")
    public ResponseEntity<EntityModel<EmailResponse>> atualizarEmail(
            @PathVariable Long id, @PathVariable Long emailId,
            @Valid @RequestBody EmailRequest request) {
        service.buscarPorId(id);
        Email email = emailService.atualizar(emailId, request);
        EntityModel<EmailResponse> model = EntityModel.of(EmailMapper.toResponse(email));
        assembler.addSubResourceLinks(model, id, "emails");
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}/emails/{emailId}")
    public ResponseEntity<Void> desassociarEmail(
            @PathVariable Long id, @PathVariable Long emailId) {
        service.desassociarEmail(id, emailId);
        return ResponseEntity.noContent().build();
    }

    // ─── Associações: documentos ──────────────────────────────────────────────

    @PostMapping("/{id}/documentos")
    public ResponseEntity<EntityModel<DocumentoResponse>> associarDocumento(
            @PathVariable Long id, @Valid @RequestBody AssociacaoDocumentoRequest request) {
        Documento doc = service.associarDocumento(id, request.getDocumentoId());
        DocumentoResponse resp = DocumentoMapper.toResponse(doc);
        EntityModel<DocumentoResponse> model = EntityModel.of(resp);
        assembler.addSubResourceLinks(model, id, "documentos");
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/documentos/{id}").buildAndExpand(doc.getId()).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}/documentos/{documentoId}")
    public ResponseEntity<EntityModel<DocumentoResponse>> atualizarDocumento(
            @PathVariable Long id, @PathVariable Long documentoId,
            @Valid @RequestBody DocumentoRequest request) {
        service.buscarPorId(id);
        Documento doc = documentoService.atualizar(documentoId, request);
        EntityModel<DocumentoResponse> model = EntityModel.of(DocumentoMapper.toResponse(doc));
        assembler.addSubResourceLinks(model, id, "documentos");
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}/documentos/{documentoId}")
    public ResponseEntity<Void> desassociarDocumento(
            @PathVariable Long id, @PathVariable Long documentoId) {
        service.desassociarDocumento(id, documentoId);
        return ResponseEntity.noContent().build();
    }

    // ─── Associações: credenciais ─────────────────────────────────────────────

    @PostMapping("/{id}/credenciais")
    public ResponseEntity<EntityModel<CredencialResponse>> associarCredencial(
            @PathVariable Long id, @Valid @RequestBody AssociacaoCredencialRequest request) {
        Credencial cred = service.associarCredencial(id, request.getCredencialId());
        CredencialResponse resp = CredencialMapper.toResponse(cred);
        EntityModel<CredencialResponse> model = EntityModel.of(resp);
        assembler.addSubResourceLinks(model, id, "credenciais");
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/credenciais/{id}").buildAndExpand(cred.getId()).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}/credenciais/{credencialId}")
    public ResponseEntity<EntityModel<CredencialResponse>> atualizarCredencial(
            @PathVariable Long id, @PathVariable Long credencialId,
            @Valid @RequestBody CredencialUpdateRequest request) {
        service.buscarPorId(id);
        Credencial cred = credencialService.atualizar(credencialId, request);
        EntityModel<CredencialResponse> model = EntityModel.of(CredencialMapper.toResponse(cred));
        assembler.addSubResourceLinks(model, id, "credenciais");
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}/credenciais/{credencialId}")
    public ResponseEntity<Void> desassociarCredencial(
            @PathVariable Long id, @PathVariable Long credencialId) {
        service.desassociarCredencial(id, credencialId);
        return ResponseEntity.noContent().build();
    }

    // ─── Associações: veículos ────────────────────────────────────────────────

    @GetMapping("/{id}/veiculos")
    @Transactional(readOnly = true)
    public ResponseEntity<CollectionModel<EntityModel<VeiculoResponse>>> listarVeiculos(
            @PathVariable Long id) {
        List<EntityModel<VeiculoResponse>> models = service.listarVeiculos(id).stream()
                .map(v -> {
                    VeiculoResponse resp = veiculoMapper.toResponse(v);
                    return EntityModel.of(veiculoAssembler.toModel(resp));
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models));
    }

    // Associa um veículo já cadastrado a este usuário (define proprietário).
    // Para trocar de dono, usar PUT /api/veiculos/{id}/proprietario/{novoId}.
    @PostMapping("/{id}/veiculos/{veiculoId}")
    public ResponseEntity<VeiculoResponse> associarVeiculo(
            @PathVariable Long id, @PathVariable Long veiculoId) {
        Veiculo veiculo = service.associarVeiculo(id, veiculoId);
        VeiculoResponse resp = veiculoMapper.toResponse(veiculo);
        resp = veiculoAssembler.toModel(resp);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/veiculos/{id}").buildAndExpand(veiculo.getId()).toUri();
        return ResponseEntity.created(location).body(resp);
    }

    // Remove apenas a associação (proprietário) — não deleta o veículo.
    // Para deletar definitivamente: DELETE /api/veiculos/{id} (após dissociar).
    @DeleteMapping("/{id}/veiculos/{veiculoId}")
    public ResponseEntity<Void> desassociarVeiculo(
            @PathVariable Long id, @PathVariable Long veiculoId) {
        service.desassociarVeiculo(id, veiculoId);
        return ResponseEntity.noContent().build();
    }
}
