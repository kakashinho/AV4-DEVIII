package com.autobots.automanager.servico;

import java.util.List;
import java.util.Set;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.dto.requisicao.EnderecoRequest;
import com.autobots.automanager.dto.requisicao.UsuarioRequest;
import com.autobots.automanager.dto.requisicao.UsuarioUpdateRequest;
import com.autobots.automanager.entidade.Credencial;
import com.autobots.automanager.entidade.Documento;
import com.autobots.automanager.entidade.Email;
import com.autobots.automanager.entidade.Endereco;
import com.autobots.automanager.entidade.Telefone;
import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.entidade.Veiculo;
import com.autobots.automanager.entidade.Venda;
import com.autobots.automanager.enumeracao.PerfilUsuario;
import com.autobots.automanager.excecao.CredencialNaoEncontradaException;
import com.autobots.automanager.excecao.DocumentoNaoEncontradoException;
import com.autobots.automanager.excecao.EmailNaoEncontradoException;
import com.autobots.automanager.excecao.RecursoJaVinculadoException;
import com.autobots.automanager.excecao.ResourceNotFoundException;
import com.autobots.automanager.excecao.TelefoneNaoEncontradoException;
import com.autobots.automanager.excecao.UsuarioComVendasException;
import com.autobots.automanager.excecao.UsuarioNaoEncontradoException;
import com.autobots.automanager.mapeador.EnderecoMapper;
import com.autobots.automanager.mapeador.UsuarioMapper;
import com.autobots.automanager.repositorio.RepositorioCredencial;
import com.autobots.automanager.repositorio.RepositorioDocumento;
import com.autobots.automanager.repositorio.RepositorioEmail;
import com.autobots.automanager.repositorio.RepositorioEndereco;
import com.autobots.automanager.repositorio.RepositorioTelefone;
import com.autobots.automanager.repositorio.RepositorioUsuario;
import com.autobots.automanager.repositorio.RepositorioVeiculo;
import com.autobots.automanager.repositorio.RepositorioVenda;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final RepositorioUsuario repositorio;
    private final RepositorioTelefone repositorioTelefone;
    private final RepositorioEmail repositorioEmail;
    private final RepositorioDocumento repositorioDocumento;
    private final RepositorioCredencial repositorioCredencial;
    private final RepositorioEndereco repositorioEndereco;
    private final RepositorioVeiculo repositorioVeiculo;
    private final RepositorioVenda repositorioVenda;
    private final UsuarioMapper mapper;

    public UsuarioServiceImpl(
            RepositorioUsuario repositorio,
            RepositorioTelefone repositorioTelefone,
            RepositorioEmail repositorioEmail,
            RepositorioDocumento repositorioDocumento,
            RepositorioCredencial repositorioCredencial,
            RepositorioEndereco repositorioEndereco,
            RepositorioVeiculo repositorioVeiculo,
            RepositorioVenda repositorioVenda,
            UsuarioMapper mapper) {
        this.repositorio = repositorio;
        this.repositorioTelefone = repositorioTelefone;
        this.repositorioEmail = repositorioEmail;
        this.repositorioDocumento = repositorioDocumento;
        this.repositorioCredencial = repositorioCredencial;
        this.repositorioEndereco = repositorioEndereco;
        this.repositorioVeiculo = repositorioVeiculo;
        this.repositorioVenda = repositorioVenda;
        this.mapper = mapper;
    }

    //  CRUD principal

    @Override
    @Transactional
    public Usuario cadastrar(UsuarioRequest request, Authentication authentication) {
        validarHierarquiaPerfis(request.getPerfis(), authentication);
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
    // /usuarios/{id}/<recurso>. enderecoId null = manter endereço existente.
    @Override
    @Transactional
    public Usuario atualizar(Long id, UsuarioUpdateRequest request, Authentication authentication) {
        Usuario usuario = buscarPorId(id);

        boolean isVendedor = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_VENDEDOR"));
        boolean isAdminOuGerente = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                            || a.getAuthority().equals("ROLE_GERENTE"));
        if (isVendedor && !isAdminOuGerente
                && !usuario.getPerfis().contains(PerfilUsuario.ROLE_CLIENTE)) {
            throw new AccessDeniedException("VENDEDOR só pode atualizar usuários com perfil CLIENTE");
        }

        usuario.setNome(request.getNome());
        if (request.getNomeSocial() != null) {
            usuario.setNomeSocial(request.getNomeSocial());
        }

        if (request.getPerfis() != null && !request.getPerfis().isEmpty()) {
            validarHierarquiaPerfis(request.getPerfis(), authentication);
            usuario.setPerfis(request.getPerfis());
        }

        if (request.getEnderecoId() != null) {
            Endereco endereco = repositorioEndereco.findById(request.getEnderecoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Endereço não encontrado com id: " + request.getEnderecoId()));
            usuario.setEndereco(endereco);
        }

        return repositorio.save(usuario);
    }

    private void validarHierarquiaPerfis(Set<PerfilUsuario> perfisSolicitados, Authentication authentication) {
        if (perfisSolicitados == null || perfisSolicitados.isEmpty()) return;

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) return;

        boolean isGerente = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_GERENTE"));
        if (isGerente) {
            if (perfisSolicitados.contains(PerfilUsuario.ROLE_ADMIN)) {
                throw new AccessDeniedException("GERENTE não pode atribuir ROLE_ADMIN");
            }
            return;
        }

        // VENDEDOR só pode atribuir CLIENTE
        Set<PerfilUsuario> permitidos = Set.of(PerfilUsuario.ROLE_CLIENTE);
        if (!permitidos.containsAll(perfisSolicitados)) {
            throw new AccessDeniedException("VENDEDOR só pode atribuir ROLE_CLIENTE");
        }
    }

    @Override
    @Transactional
    public void remover(Long id, Authentication authentication) {
        Usuario alvo = buscarPorId(id);
        boolean solicitanteEhAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!solicitanteEhAdmin) {
            boolean alvoEhAdminOuGerente = alvo.getPerfis().contains(PerfilUsuario.ROLE_ADMIN)
                    || alvo.getPerfis().contains(PerfilUsuario.ROLE_GERENTE);
            if (alvoEhAdminOuGerente) {
                throw new AccessDeniedException("Sem permissão para remover este usuário");
            }
        }
        if (repositorioVenda.existsByClienteId(id) || repositorioVenda.existsByFuncionarioId(id)) {
            throw new UsuarioComVendasException(id);
        }
        alvo.getTelefones().clear();
        alvo.getEmails().clear();
        alvo.getDocumentos().clear();
        alvo.getCredenciais().clear();
        alvo.setEndereco(null);
        alvo.setEmpresa(null);
        repositorio.save(alvo);
        repositorio.delete(alvo);
    }

    //  Associações: telefones 

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

    //  Associações: emails 

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

    //  Associações: documentos 

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

    //  Associações: credenciais 

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

    //  Associações: veículos 

    @Override
    @Transactional(readOnly = true)
    public List<Veiculo> listarVeiculos(Long usuarioId) {
        buscarPorId(usuarioId);
        return repositorioVeiculo.findByProprietarioId(usuarioId);
    }

    // Associa um veículo já existente ao usuário (define proprietario).
    // Recusa se o veículo já tem outro proprietário.
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
        repositorioVeiculo.save(veiculo);
        return veiculo;
    }

    // Remove a associação (proprietário) sem deletar o veículo.
    @Override
    @Transactional
    public void desassociarVeiculo(Long usuarioId, Long veiculoId) {
        Veiculo veiculo = repositorioVeiculo.findById(veiculoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Veículo " + veiculoId + " não encontrado"));

        if (veiculo.getProprietario() == null
                || !veiculo.getProprietario().getId().equals(usuarioId)) {
            throw new ResourceNotFoundException(
                    "Veículo " + veiculoId + " não está associado ao usuário " + usuarioId);
        }

        veiculo.setProprietario(null);
        repositorioVeiculo.save(veiculo);
    }

    //  Empresa

    @Override
    @Transactional
    public void desassociarEmpresa(Long usuarioId) {
        Usuario usuario = buscarPorId(usuarioId);
        usuario.setEmpresa(null);
        repositorio.save(usuario);
    }

    //  Endereço

    @Override
    @Transactional(readOnly = true)
    public Endereco buscarEndereco(Long usuarioId) {
        Usuario usuario = buscarPorId(usuarioId);
        if (usuario.getEndereco() == null) {
            throw new ResourceNotFoundException("Usuário " + usuarioId + " não possui endereço cadastrado");
        }
        return usuario.getEndereco();
    }

    // Cria novo endereço e o associa ao usuário.
    // Se o usuário já tinha endereço, o endereço anterior é desvinculado (não deletado).
    @Override
    @Transactional
    public Endereco definirEndereco(Long usuarioId, EnderecoRequest request) {
        Usuario usuario = buscarPorId(usuarioId);
        Endereco novo = EnderecoMapper.toEntity(request);
        Endereco salvo = repositorioEndereco.save(novo);
        usuario.setEndereco(salvo);
        repositorio.save(usuario);
        return salvo;
    }

    @Override
    @Transactional
    public void removerEndereco(Long usuarioId) {
        Usuario usuario = buscarPorId(usuarioId);
        if (usuario.getEndereco() == null) {
            throw new ResourceNotFoundException("Usuário " + usuarioId + " não possui endereço cadastrado");
        }
        usuario.setEndereco(null);
        repositorio.save(usuario);
    }

    //  Vendas do usuário

    // Retorna vendas onde o usuário é cliente OU funcionário.
    // CLIENTE só vê as próprias; ADMIN/GERENTE/VENDEDOR vê as do usuário alvo.
    @Override
    @Transactional(readOnly = true)
    public List<Venda> listarVendas(Long usuarioId, Authentication authentication) {
        buscarPorId(usuarioId);
        boolean isCliente = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"));
        if (isCliente) {
            // CLIENTE só pode ver as próprias vendas
            Usuario autenticado = repositorio.findByCredencialNomeUsuario(authentication.getName())
                    .or(() -> {
                        if (authentication.getName().startsWith("CB:")) {
                            long cod = Long.parseLong(authentication.getName().substring(3));
                            return repositorio.findByCredencialCodigoBarra(cod);
                        }
                        return java.util.Optional.empty();
                    })
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário autenticado não encontrado"));
            if (!autenticado.getId().equals(usuarioId)) {
                throw new org.springframework.security.access.AccessDeniedException(
                        "CLIENTE só pode visualizar as próprias vendas");
            }
        }
        List<Venda> vendas = repositorioVenda.findByUsuarioIdComItens(usuarioId);
        vendas.forEach(v -> v.getServicos().size());
        return vendas;
    }

    // Associa uma venda existente ao usuário como cliente ou funcionário.
    // Apenas ADMIN e GERENTE podem fazer essa operação.
    @Override
    @Transactional
    public Venda associarVenda(Long usuarioId, Long vendaId, Authentication authentication) {
        buscarPorId(usuarioId);
        Venda venda = repositorioVenda.findByIdComItens(vendaId)
                .orElseThrow(() -> new ResourceNotFoundException("Venda " + vendaId + " não encontrada"));
        venda.getServicos().size();

        boolean jaAssociado = usuarioId.equals(venda.getClienteId())
                || usuarioId.equals(venda.getFuncionarioId());
        if (jaAssociado) {
            throw new RecursoJaVinculadoException("Usuário " + usuarioId + " já está associado à venda " + vendaId);
        }

        // Associa como cliente se a venda ainda não tiver cliente; senão como funcionário
        if (venda.getClienteId() == null) {
            Usuario usuario = buscarPorId(usuarioId);
            venda.setClienteId(usuarioId);
            venda.setClienteNomeSnapshot(usuario.getNome());
        } else if (venda.getFuncionarioId() == null) {
            Usuario usuario = buscarPorId(usuarioId);
            venda.setFuncionarioId(usuarioId);
            venda.setFuncionarioNomeSnapshot(usuario.getNome());
        } else {
            throw new RecursoJaVinculadoException("Venda " + vendaId + " já possui cliente e funcionário");
        }

        return repositorioVenda.save(venda);
    }

    // Remove a associação do usuário à venda (zera clienteId ou funcionarioId).
    @Override
    @Transactional
    public void desassociarVenda(Long usuarioId, Long vendaId, Authentication authentication) {
        buscarPorId(usuarioId);
        Venda venda = repositorioVenda.findByIdComItens(vendaId)
                .orElseThrow(() -> new ResourceNotFoundException("Venda " + vendaId + " não encontrada"));
        venda.getServicos().size();

        if (usuarioId.equals(venda.getClienteId())) {
            venda.setClienteId(null);
            venda.setClienteNomeSnapshot(null);
        } else if (usuarioId.equals(venda.getFuncionarioId())) {
            venda.setFuncionarioId(null);
            venda.setFuncionarioNomeSnapshot(null);
        } else {
            throw new ResourceNotFoundException(
                    "Usuário " + usuarioId + " não está associado à venda " + vendaId);
        }

        repositorioVenda.save(venda);
    }
}
