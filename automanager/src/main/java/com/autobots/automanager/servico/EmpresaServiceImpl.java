package com.autobots.automanager.servico;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.dto.EmpresaResumo;
import com.autobots.automanager.dto.requisicao.EmpresaRequest;
import com.autobots.automanager.dto.requisicao.EmpresaUpdateRequest;
import com.autobots.automanager.dto.requisicao.MercadoriaRequest;
import com.autobots.automanager.dto.requisicao.ServicoRequest;
import com.autobots.automanager.dto.requisicao.VendaRequest;
import com.autobots.automanager.dto.resposta.EmpresaResponse;
import com.autobots.automanager.dto.resposta.MercadoriaResponse;
import com.autobots.automanager.dto.resposta.ServicoResponse;
import com.autobots.automanager.dto.resposta.UsuarioReferencia;
import com.autobots.automanager.dto.resposta.VendaResponse;
import com.autobots.automanager.entidade.Empresa;
import com.autobots.automanager.entidade.Mercadoria;
import com.autobots.automanager.entidade.Servico;
import com.autobots.automanager.entidade.Telefone;
import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.entidade.Venda;
import com.autobots.automanager.enumeracao.PerfilUsuario;
import org.springframework.security.core.Authentication;
import com.autobots.automanager.excecao.EmpresaComVendasException;
import com.autobots.automanager.excecao.EmpresaNaoEncontradaException;
import com.autobots.automanager.excecao.MercadoriaEmUsoException;
import com.autobots.automanager.excecao.MercadoriaNaoEncontradaException;
import com.autobots.automanager.excecao.RecursoJaVinculadoException;
import com.autobots.automanager.excecao.ResourceNotFoundException;
import com.autobots.automanager.excecao.ServicoEmUsoException;
import com.autobots.automanager.excecao.ServicoNaoEncontradoException;
import com.autobots.automanager.excecao.UsuarioJaAssociadoException;
import com.autobots.automanager.excecao.UsuarioNaoAssociadoException;
import com.autobots.automanager.excecao.UsuarioNaoEncontradoException;
import com.autobots.automanager.mapeador.EmpresaMapper;
import com.autobots.automanager.mapeador.MercadoriaMapper;
import com.autobots.automanager.mapeador.ServicoMapper;
import com.autobots.automanager.mapeador.VendaMapper;
import com.autobots.automanager.excecao.TelefoneNaoEncontradoException;
import com.autobots.automanager.repositorio.RepositorioEmpresa;
import com.autobots.automanager.repositorio.RepositorioEndereco;
import com.autobots.automanager.repositorio.RepositorioMercadoria;
import com.autobots.automanager.repositorio.RepositorioServico;
import com.autobots.automanager.repositorio.RepositorioTelefone;
import com.autobots.automanager.repositorio.RepositorioUsuario;
import com.autobots.automanager.repositorio.RepositorioVenda;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private final RepositorioEmpresa repositorio;
    private final RepositorioUsuario usuarioRepo;
    private final RepositorioMercadoria mercadoriaRepo;
    private final RepositorioServico servicoRepo;
    private final RepositorioVenda vendaRepo;
    private final RepositorioTelefone telefoneRepo;
    private final RepositorioEndereco enderecoRepo;
    private final EmpresaMapper mapper;
    private final MercadoriaMapper mercadoriaMapper;
    private final ServicoMapper servicoMapper;
    private final VendaMapper vendaMapper;
    private final UsuarioService usuarioService;
    private final MercadoriaService mercadoriaService;
    private final VendaService vendaService;

    public EmpresaServiceImpl(
            RepositorioEmpresa repositorio,
            RepositorioUsuario usuarioRepo,
            RepositorioMercadoria mercadoriaRepo,
            RepositorioServico servicoRepo,
            RepositorioVenda vendaRepo,
            RepositorioTelefone telefoneRepo,
            RepositorioEndereco enderecoRepo,
            EmpresaMapper mapper,
            MercadoriaMapper mercadoriaMapper,
            ServicoMapper servicoMapper,
            VendaMapper vendaMapper,
            UsuarioService usuarioService,
            MercadoriaService mercadoriaService,
            VendaService vendaService) {

        this.repositorio = repositorio;
        this.usuarioRepo = usuarioRepo;
        this.mercadoriaRepo = mercadoriaRepo;
        this.servicoRepo = servicoRepo;
        this.vendaRepo = vendaRepo;
        this.telefoneRepo = telefoneRepo;
        this.enderecoRepo = enderecoRepo;
        this.mapper = mapper;
        this.mercadoriaMapper = mercadoriaMapper;
        this.servicoMapper = servicoMapper;
        this.vendaMapper = vendaMapper;
        this.usuarioService = usuarioService;
        this.mercadoriaService = mercadoriaService;
        this.vendaService = vendaService;
    }

    //  CRUD principal 

    @Override
    @Transactional(readOnly = true)
    public List<EmpresaResumo> listarTodas() {
        return repositorio.findAll().stream()
                .map(mapper::toResumo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmpresaResponse buscarPorId(Long id) {
        return montarResponse(obterEntidade(id));
    }

    @Override
    @Transactional
    public EmpresaResponse cadastrar(EmpresaRequest request) {
        Empresa empresa = mapper.toEntity(request);
        return montarResponse(repositorio.save(empresa));
    }

    @Override
    @Transactional
    public EmpresaResponse atualizar(Long id, EmpresaUpdateRequest request) {
        Empresa empresa = obterEntidade(id);
        mapper.aplicarUpdate(empresa, request);
        return montarResponse(repositorio.save(empresa));
    }

    @Override
    @Transactional
    public void remover(Long id) {
        Empresa empresa = obterEntidade(id);

        long totalVendas = vendaRepo.countByEmpresaId(id);
        if (totalVendas > 0) {
            throw new EmpresaComVendasException(id, totalVendas);
        }

        usuarioRepo.findByEmpresaId(id)
                .forEach(u -> usuarioService.desassociarEmpresa(u.getId()));

        mercadoriaRepo.findByEmpresaId(id)
                .forEach(m -> mercadoriaService.desassociarEmpresa(m.getId()));

        repositorio.delete(empresa);
    }

    //  Usuários da empresa 

    @Override
    @Transactional(readOnly = true)
    public Set<UsuarioReferencia> listarUsuarios(Long empresaId, PerfilUsuario perfil) {
        obterEntidade(empresaId);

        List<Usuario> usuarios = (perfil == null)
                ? usuarioRepo.findByEmpresaId(empresaId)
                : usuarioRepo.findByEmpresaIdAndPerfisContaining(empresaId, perfil);

        return usuarios.stream()
                .map(this::paraReferencia)
                .collect(Collectors.toCollection(java.util.LinkedHashSet::new));
    }

    @Override
    @Transactional
    public void associarUsuario(Long empresaId, Long usuarioId) {
        Empresa empresa = obterEntidade(empresaId);
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));

        if (usuario.getEmpresa() != null) {
            throw new UsuarioJaAssociadoException(usuarioId, usuario.getEmpresa().getId());
        }

        usuario.setEmpresa(empresa);
        usuarioRepo.save(usuario);
    }

    @Override
    @Transactional
    public void desassociarUsuario(Long empresaId, Long usuarioId) {
        obterEntidade(empresaId);

        Usuario usuario = usuarioRepo.findByIdAndEmpresaId(usuarioId, empresaId)
                .orElseThrow(() -> new UsuarioNaoAssociadoException(usuarioId, empresaId));

        usuario.setEmpresa(null);
        usuarioRepo.save(usuario);
    }

    //  Telefones da empresa

    @Override
    @Transactional
    public void associarTelefone(Long empresaId, Long telefoneId) {
        Empresa empresa = obterEntidade(empresaId);
        Telefone telefone = telefoneRepo.findById(telefoneId)
                .orElseThrow(() -> new TelefoneNaoEncontradoException(telefoneId));
        if (empresa.getTelefones().stream().anyMatch(t -> t.getId().equals(telefoneId))) {
            throw new RecursoJaVinculadoException("Telefone " + telefoneId + " já está vinculado à empresa " + empresaId);
        }
        empresa.getTelefones().add(telefone);
        repositorio.save(empresa);
    }

    @Override
    @Transactional
    public void desassociarTelefone(Long empresaId, Long telefoneId) {
        Empresa empresa = obterEntidade(empresaId);
        boolean removido = empresa.getTelefones().removeIf(t -> t.getId().equals(telefoneId));
        if (!removido) {
            throw new TelefoneNaoEncontradoException(telefoneId);
        }
        repositorio.save(empresa);
    }

    //  Endereço da empresa

    @Override
    @Transactional
    public void removerEndereco(Long empresaId) {
        Empresa empresa = obterEntidade(empresaId);
        empresa.setEndereco(null);
        repositorio.save(empresa);
    }

    //  Mercadorias da empresa

    @Override
    @Transactional(readOnly = true)
    public List<MercadoriaResponse> listarMercadorias(Long empresaId) {
        obterEntidade(empresaId);
        return mercadoriaRepo.findByEmpresaId(empresaId).stream()
                .map(mercadoriaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MercadoriaResponse criarMercadoria(Long empresaId, MercadoriaRequest request) {
        Empresa empresa = obterEntidade(empresaId);
        Mercadoria mercadoria = mercadoriaMapper.toEntity(request);
        mercadoria.setEmpresa(empresa);
        return mercadoriaMapper.toResponse(mercadoriaRepo.save(mercadoria));
    }

    @Override
    @Transactional
    public MercadoriaResponse associarMercadoria(Long empresaId, Long mercadoriaId) {
        Empresa empresa = obterEntidade(empresaId);
        Mercadoria mercadoria = mercadoriaRepo.findById(mercadoriaId)
                .orElseThrow(() -> new MercadoriaNaoEncontradaException(mercadoriaId));
        mercadoria.setEmpresa(empresa);
        return mercadoriaMapper.toResponse(mercadoriaRepo.save(mercadoria));
    }

    @Override
    @Transactional
    public void desassociarMercadoria(Long empresaId, Long mercadoriaId) {
        obterEntidade(empresaId);
        Mercadoria mercadoria = mercadoriaRepo.findById(mercadoriaId)
                .orElseThrow(() -> new MercadoriaNaoEncontradaException(mercadoriaId));

        if (mercadoria.getEmpresa() == null || !mercadoria.getEmpresa().getId().equals(empresaId)) {
            throw new MercadoriaNaoEncontradaException(mercadoriaId);
        }

        if (vendaRepo.existsByItensMercadoriaId(mercadoriaId)) {
            throw new MercadoriaEmUsoException(mercadoriaId);
        }

        mercadoria.setEmpresa(null);
        mercadoriaRepo.save(mercadoria);
    }

    //  Serviços da empresa 

    @Override
    @Transactional(readOnly = true)
    public List<ServicoResponse> listarServicos(Long empresaId) {
        obterEntidade(empresaId);
        return servicoRepo.findByEmpresaId(empresaId).stream()
                .map(servicoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ServicoResponse criarServico(Long empresaId, ServicoRequest request) {
        Empresa empresa = obterEntidade(empresaId);
        Servico servico = servicoMapper.toEntity(request);
        servico.setEmpresa(empresa);
        return servicoMapper.toResponse(servicoRepo.save(servico));
    }

    @Override
    @Transactional
    public void associarServico(Long empresaId, Long servicoId) {
        Empresa empresa = obterEntidade(empresaId);
        Servico servico = servicoRepo.findById(servicoId)
                .orElseThrow(() -> new ServicoNaoEncontradoException(servicoId));

        if (servico.getEmpresa() != null) {
            throw new RecursoJaVinculadoException(
                    "Serviço " + servicoId + " já está vinculado à empresa " + servico.getEmpresa().getId());
        }

        servico.setEmpresa(empresa);
        servicoRepo.save(servico);
    }

    @Override
    @Transactional
    public void desassociarServico(Long empresaId, Long servicoId) {
        obterEntidade(empresaId);
        Servico servico = servicoRepo.findByIdAndEmpresaId(servicoId, empresaId)
                .orElseThrow(() -> new ServicoNaoEncontradoException(servicoId));

        if (vendaRepo.existsByServicosServicoId(servicoId)) {
            throw new ServicoEmUsoException(servicoId);
        }

        servico.setEmpresa(null);
        servicoRepo.save(servico);
    }

    //  Vendas da empresa — retornam DTOs (entidade JPA não cruza fronteira) 

    @Override
    @Transactional(readOnly = true)
    public List<VendaResponse> listarVendas(Long empresaId) {
        obterEntidade(empresaId);
        List<Venda> vendas = vendaRepo.findByEmpresaIdComItens(empresaId);
        vendas.forEach(v -> v.getServicos().size());
        return vendas.stream()
                .map(vendaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VendaResponse obterVenda(Long empresaId, Long vendaId) {
        obterEntidade(empresaId);
        Venda venda = vendaRepo.findByIdAndEmpresaIdComItens(vendaId, empresaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Venda " + vendaId + " não encontrada na empresa " + empresaId));
        venda.getServicos().size();
        return vendaMapper.toResponse(venda);
    }

    @Override
    @Transactional
    public VendaResponse criarVenda(Long empresaId, VendaRequest request, Authentication authentication) {
        obterEntidade(empresaId);
        request.setEmpresaId(empresaId);
        Venda venda = vendaService.criarVenda(request, authentication);
        return vendaMapper.toResponse(venda);
    }

    @Override
    @Transactional
    public void associarVenda(Long empresaId, Long vendaId) {
        obterEntidade(empresaId);
        Venda venda = vendaRepo.findById(vendaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Venda não encontrada com id: " + vendaId));

        if (venda.getEmpresaId() != null) {
            throw new RecursoJaVinculadoException(
                    "Venda " + vendaId + " já está vinculada à empresa " + venda.getEmpresaId());
        }

        venda.setEmpresaId(empresaId);
        vendaRepo.save(venda);
    }

    @Override
    @Transactional(readOnly = true)
    public void desassociarVenda(Long empresaId, Long vendaId) {
        throw new UnsupportedOperationException(
                "Desassociar uma venda de uma empresa viola a integridade contábil. " +
                "Vendas são registros imutáveis vinculados permanentemente à empresa que as realizou.");
    }

    //  Apoio / privados 

    @Override
    @Transactional(readOnly = true)
    public Empresa obterEmpresa(Long id) {
        return obterEntidade(id);
    }

    private Empresa obterEntidade(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new EmpresaNaoEncontradaException(id));
    }

    private EmpresaResponse montarResponse(Empresa empresa) {
        EmpresaResponse response = mapper.toResponse(empresa);
        Long id = empresa.getId();
        response.setTotalUsuarios(usuarioRepo.countByEmpresaId(id));
        response.setTotalMercadorias(mercadoriaRepo.countByEmpresaId(id));
        response.setTotalServicos(servicoRepo.countByEmpresaId(id));
        response.setTotalVendas(vendaRepo.countByEmpresaId(id));
        return response;
    }

    private UsuarioReferencia paraReferencia(Usuario usuario) {
        UsuarioReferencia ref = new UsuarioReferencia();
        ref.setId(usuario.getId());
        ref.setNome(usuario.getNome());
        if (!usuario.getEmails().isEmpty()) {
            ref.setEmail(usuario.getEmails().iterator().next().getEndereco());
        }
        return ref;
    }
}
