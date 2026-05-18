package com.autobots.automanager.servico;

import com.autobots.automanager.dto.requisicao.UsuarioRequest;
import com.autobots.automanager.dto.requisicao.UsuarioUpdateRequest;
import com.autobots.automanager.entidade.Credencial;
import com.autobots.automanager.entidade.Documento;
import com.autobots.automanager.entidade.Email;
import com.autobots.automanager.entidade.Telefone;
import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.entidade.Veiculo;
import com.autobots.automanager.excecao.CredencialNaoEncontradaException;
import com.autobots.automanager.excecao.DocumentoNaoEncontradoException;
import com.autobots.automanager.excecao.EmailNaoEncontradoException;
import com.autobots.automanager.excecao.RecursoJaVinculadoException;
import com.autobots.automanager.excecao.ResourceNotFoundException;
import com.autobots.automanager.excecao.TelefoneNaoEncontradoException;
import com.autobots.automanager.excecao.UsuarioNaoEncontradoException;
import com.autobots.automanager.mapeador.UsuarioMapper;
import com.autobots.automanager.repositorio.RepositorioCredencial;
import com.autobots.automanager.repositorio.RepositorioDocumento;
import com.autobots.automanager.repositorio.RepositorioEmail;
import com.autobots.automanager.repositorio.RepositorioTelefone;
import com.autobots.automanager.repositorio.RepositorioUsuario;
import com.autobots.automanager.repositorio.RepositorioVeiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final RepositorioUsuario repositorio;
    private final RepositorioTelefone repositorioTelefone;
    private final RepositorioEmail repositorioEmail;
    private final RepositorioDocumento repositorioDocumento;
    private final RepositorioCredencial repositorioCredencial;
    private final UsuarioMapper mapper;

    @Autowired
    private RepositorioVeiculo repositorioVeiculo;

    public UsuarioServiceImpl(
            RepositorioUsuario repositorio,
            RepositorioTelefone repositorioTelefone,
            RepositorioEmail repositorioEmail,
            RepositorioDocumento repositorioDocumento,
            RepositorioCredencial repositorioCredencial,
            UsuarioMapper mapper) {
        this.repositorio = repositorio;
        this.repositorioTelefone = repositorioTelefone;
        this.repositorioEmail = repositorioEmail;
        this.repositorioDocumento = repositorioDocumento;
        this.repositorioCredencial = repositorioCredencial;
        this.mapper = mapper;
    }

    // ─── CRUD principal ───────────────────────────────────────────────────────

    @Override
    @Transactional
    public Usuario cadastrar(UsuarioRequest request) {
        Usuario usuario = mapper.paraEntidade(request);
        return repositorio.save(usuario);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }

    @Override
    public List<Usuario> listarTodos() {
        return repositorio.findAll();
    }

    // Atualização parcial controlada: telefones, emails, documentos e credenciais
    // NÃO estão no UsuarioUpdateRequest — só podem ser modificados via
    // /usuarios/{id}/<recurso>. Endereco e perfis só são alterados se enviados
    // (null = manter). Nunca apaga dados que o cliente não enviou explicitamente.
    @Override
    @Transactional
    public Usuario atualizar(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = buscarPorId(id);

        usuario.setNome(request.getNome());
        if (request.getNomeSocial() != null) {
            usuario.setNomeSocial(request.getNomeSocial());
        }

        if (request.getPerfis() != null && !request.getPerfis().isEmpty()) {
            usuario.setPerfis(request.getPerfis());
        }

        if (request.getEndereco() != null) {
            usuario.setEndereco(mapper.paraEntidadeEndereco(request.getEndereco()));
        }

        return repositorio.save(usuario);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        repositorio.delete(buscarPorId(id));
    }

    // ─── Associações: telefones ───────────────────────────────────────────────

    @Override
    @Transactional
    public Telefone associarTelefone(Long usuarioId, Long telefoneId) {
        Usuario usuario = buscarPorId(usuarioId);
        Telefone telefone = repositorioTelefone.findById(telefoneId)
                .orElseThrow(() -> new TelefoneNaoEncontradoException(telefoneId));

        boolean jaAssociado = usuario.getTelefones().stream()
                .anyMatch(t -> t.getId().equals(telefoneId));
        if (jaAssociado) {
            throw new ResourceNotFoundException(
                    "Telefone " + telefoneId + " já está associado ao usuário " + usuarioId);
        }

        usuario.getTelefones().add(telefone);
        repositorio.save(usuario);
        return telefone;
    }

    @Override
    @Transactional
    public void desassociarTelefone(Long usuarioId, Long telefoneId) {
        Usuario usuario = buscarPorId(usuarioId);
        boolean removido = usuario.getTelefones()
                .removeIf(t -> t.getId() != null && t.getId().equals(telefoneId));
        if (!removido) {
            throw new ResourceNotFoundException(
                    "Telefone " + telefoneId + " não está associado ao usuário " + usuarioId);
        }
        repositorio.save(usuario);
    }

    // ─── Associações: emails ──────────────────────────────────────────────────

    @Override
    @Transactional
    public Email associarEmail(Long usuarioId, Long emailId) {
        Usuario usuario = buscarPorId(usuarioId);
        Email email = repositorioEmail.findById(emailId)
                .orElseThrow(() -> new EmailNaoEncontradoException(emailId));

        if (repositorioEmail.existeAssociadoAOutroUsuario(emailId, usuarioId)) {
            throw new RecursoJaVinculadoException("Recurso já vinculado a outro usuário");
        }

        boolean jaAssociado = usuario.getEmails().stream()
                .anyMatch(e -> e.getId().equals(emailId));
        if (jaAssociado) {
            throw new RecursoJaVinculadoException("Email " + emailId + " já está associado ao usuário " + usuarioId);
        }

        usuario.getEmails().add(email);
        repositorio.save(usuario);
        return email;
    }

    @Override
    @Transactional
    public void desassociarEmail(Long usuarioId, Long emailId) {
        Usuario usuario = buscarPorId(usuarioId);
        boolean removido = usuario.getEmails()
                .removeIf(e -> e.getId() != null && e.getId().equals(emailId));
        if (!removido) {
            throw new ResourceNotFoundException(
                    "Email " + emailId + " não está associado ao usuário " + usuarioId);
        }
        repositorio.save(usuario);
    }

    // ─── Associações: documentos ──────────────────────────────────────────────

    @Override
    @Transactional
    public Documento associarDocumento(Long usuarioId, Long documentoId) {
        Usuario usuario = buscarPorId(usuarioId);
        Documento documento = repositorioDocumento.findById(documentoId)
                .orElseThrow(() -> new DocumentoNaoEncontradoException(documentoId));

        if (repositorioDocumento.existeAssociadoAOutroUsuario(documentoId, usuarioId)) {
            throw new RecursoJaVinculadoException("Recurso já vinculado a outro usuário");
        }

        boolean jaAssociado = usuario.getDocumentos().stream()
                .anyMatch(d -> d.getId().equals(documentoId));
        if (jaAssociado) {
            throw new RecursoJaVinculadoException("Documento " + documentoId + " já está associado ao usuário " + usuarioId);
        }

        usuario.getDocumentos().add(documento);
        repositorio.save(usuario);
        return documento;
    }

    @Override
    @Transactional
    public void desassociarDocumento(Long usuarioId, Long documentoId) {
        Usuario usuario = buscarPorId(usuarioId);
        boolean removido = usuario.getDocumentos()
                .removeIf(d -> d.getId() != null && d.getId().equals(documentoId));
        if (!removido) {
            throw new ResourceNotFoundException(
                    "Documento " + documentoId + " não está associado ao usuário " + usuarioId);
        }
        repositorio.save(usuario);
    }

    // ─── Associações: credenciais ─────────────────────────────────────────────

    @Override
    @Transactional
    public Credencial associarCredencial(Long usuarioId, Long credencialId) {
        Usuario usuario = buscarPorId(usuarioId);
        Credencial credencial = repositorioCredencial.findById(credencialId)
                .orElseThrow(() -> new CredencialNaoEncontradaException(credencialId));

        if (repositorioCredencial.existeAssociadoAOutroUsuario(credencialId, usuarioId)) {
            throw new RecursoJaVinculadoException("Recurso já vinculado a outro usuário");
        }

        boolean jaAssociado = usuario.getCredenciais().stream()
                .anyMatch(c -> c.getId().equals(credencialId));
        if (jaAssociado) {
            throw new RecursoJaVinculadoException("Credencial " + credencialId + " já está associada ao usuário " + usuarioId);
        }

        usuario.getCredenciais().add(credencial);
        repositorio.save(usuario);
        return credencial;
    }

    @Override
    @Transactional
    public void desassociarCredencial(Long usuarioId, Long credencialId) {
        Usuario usuario = buscarPorId(usuarioId);
        boolean removido = usuario.getCredenciais()
                .removeIf(c -> c.getId() != null && c.getId().equals(credencialId));
        if (!removido) {
            throw new ResourceNotFoundException(
                    "Credencial " + credencialId + " não está associada ao usuário " + usuarioId);
        }
        repositorio.save(usuario);
    }

    // ─── Associações: veículos ────────────────────────────────────────────────

    // Associa um veículo já existente ao usuário (define proprietario e adiciona
    // à coleção). Recusa se o veículo já tem outro proprietário — para trocar
    // de dono usar PUT /veiculos/{id}/proprietario/{novoUsuarioId}.
    @Override
    @Transactional
    public Veiculo associarVeiculo(Long usuarioId, Long veiculoId) {
        Usuario usuario = buscarPorId(usuarioId);
        Veiculo veiculo = repositorioVeiculo.findById(veiculoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Veículo " + veiculoId + " não encontrado"));

        if (veiculo.getProprietario() != null
                && !veiculo.getProprietario().getId().equals(usuarioId)) {
            throw new RecursoJaVinculadoException(
                    "Veículo " + veiculoId + " já pertence ao usuário "
                    + veiculo.getProprietario().getId()
                    + ". Use a transferência de propriedade.");
        }

        veiculo.setProprietario(usuario);
        usuario.getVeiculos().add(veiculo);
        repositorioVeiculo.save(veiculo);
        return veiculo;
    }

    // Remove a associação sem deletar o veículo: limpa proprietario e remove
    // da coleção do usuário. O veículo permanece no sistema, agora órfão.
    @Override
    @Transactional
    public void desassociarVeiculo(Long usuarioId, Long veiculoId) {
        Usuario usuario = buscarPorId(usuarioId);
        Veiculo veiculo = repositorioVeiculo.findById(veiculoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Veículo " + veiculoId + " não encontrado"));

        if (veiculo.getProprietario() == null
                || !veiculo.getProprietario().getId().equals(usuarioId)) {
            throw new ResourceNotFoundException(
                    "Veículo " + veiculoId + " não está associado ao usuário " + usuarioId);
        }

        veiculo.setProprietario(null);
        usuario.getVeiculos().removeIf(v -> v.getId().equals(veiculoId));
        repositorioVeiculo.save(veiculo);
    }
}
