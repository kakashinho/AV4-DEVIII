package com.autobots.automanager.servico;

import java.util.ArrayList;
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
import com.autobots.automanager.entidade.Empresa;
import com.autobots.automanager.entidade.Mercadoria;
import com.autobots.automanager.entidade.Servico;
import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.entidade.Venda;
import com.autobots.automanager.enumeracao.PerfilUsuario;
import com.autobots.automanager.excecao.EmpresaComVendasException;
import com.autobots.automanager.excecao.EmpresaNaoEncontradaException;
import com.autobots.automanager.excecao.MercadoriaEmUsoException;
import com.autobots.automanager.excecao.MercadoriaNaoEncontradaException;
import com.autobots.automanager.excecao.ResourceNotFoundException;
import com.autobots.automanager.excecao.ServicoNaoEncontradoException;
import com.autobots.automanager.excecao.UsuarioJaAssociadoException;
import com.autobots.automanager.excecao.UsuarioNaoAssociadoException;
import com.autobots.automanager.excecao.UsuarioNaoEncontradoException;
import com.autobots.automanager.mapeador.EmpresaMapper;
import com.autobots.automanager.mapeador.MercadoriaMapper;
import com.autobots.automanager.mapeador.ServicoMapper;
import com.autobots.automanager.repositorio.RepositorioEmpresa;
import com.autobots.automanager.repositorio.RepositorioMercadoria;
import com.autobots.automanager.repositorio.RepositorioServico;
import com.autobots.automanager.repositorio.RepositorioUsuario;
import com.autobots.automanager.repositorio.RepositorioVenda;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private final RepositorioEmpresa repositorio;
    private final RepositorioUsuario usuarioRepo;
    private final RepositorioMercadoria mercadoriaRepo;
    private final RepositorioServico servicoRepo;
    private final RepositorioVenda vendaRepo;
    private final EmpresaMapper mapper;
    private final MercadoriaMapper mercadoriaMapper;
    private final ServicoMapper servicoMapper;
    private final VendaService vendaService;

    public EmpresaServiceImpl(
            RepositorioEmpresa repositorio,
            RepositorioUsuario usuarioRepo,
            RepositorioMercadoria mercadoriaRepo,
            RepositorioServico servicoRepo,
            RepositorioVenda vendaRepo,
            EmpresaMapper mapper,
            MercadoriaMapper mercadoriaMapper,
            ServicoMapper servicoMapper,
            VendaService vendaService) {

        this.repositorio = repositorio;
        this.usuarioRepo = usuarioRepo;
        this.mercadoriaRepo = mercadoriaRepo;
        this.servicoRepo = servicoRepo;
        this.vendaRepo = vendaRepo;
        this.mapper = mapper;
        this.mercadoriaMapper = mercadoriaMapper;
        this.servicoMapper = servicoMapper;
        this.vendaService = vendaService;
    }

    // ─── CRUD principal ───────────────────────────────────────────────────────

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
        Empresa empresa = obterEntidade(id);
        return montarResponse(empresa);
    }

    @Override
    @Transactional
    public EmpresaResponse cadastrar(EmpresaRequest request) {
        Empresa empresa = mapper.toEntity(request);
        Empresa salva = repositorio.save(empresa);
        return montarResponse(salva);
    }

    @Override
    @Transactional
    public EmpresaResponse atualizar(Long id, EmpresaUpdateRequest request) {
        Empresa empresa = obterEntidade(id);
        mapper.aplicarUpdate(empresa, request);
        Empresa salva = repositorio.save(empresa);
        return montarResponse(salva);
    }

    // Deleta empresa com guards:
    //  1) Vendas registradas bloqueiam o delete (histórico contábil).
    //  2) Usuários são desvinculados (preserva a entidade Usuario).
    //  3) Mercadorias são desvinculadas (mercadoria pode persistir num fornecedor).
    //  4) Serviços (cascade ALL + orphanRemoval=true) caem junto com a empresa.
    @Override
    @Transactional
    public void remover(Long id) {
        Empresa empresa = obterEntidade(id);

        long totalVendas = vendaRepo.countByEmpresaId(id);
        if (totalVendas > 0) {
            throw new EmpresaComVendasException(id, totalVendas);
        }

        for (Usuario u : new ArrayList<>(empresa.getUsuarios())) {
            u.setEmpresa(null);
            usuarioRepo.save(u);
        }
        empresa.getUsuarios().clear();

        for (Mercadoria m : new ArrayList<>(empresa.getMercadorias())) {
            m.setEmpresa(null);
            mercadoriaRepo.save(m);
        }
        empresa.getMercadorias().clear();

        repositorio.delete(empresa);
    }

    // ─── Usuários da empresa ──────────────────────────────────────────────────

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
        empresa.getUsuarios().add(usuario);
        usuarioRepo.save(usuario);
    }

    @Override
    @Transactional
    public void desassociarUsuario(Long empresaId, Long usuarioId) {
        Empresa empresa = obterEntidade(empresaId);

        Usuario usuario = empresa.getUsuarios().stream()
                .filter(u -> u.getId().equals(usuarioId))
                .findFirst()
                .orElseThrow(() -> new UsuarioNaoAssociadoException(usuarioId, empresaId));

        empresa.getUsuarios().remove(usuario);
        usuario.setEmpresa(null);
        usuarioRepo.save(usuario);
    }

    // ─── Mercadorias da empresa ───────────────────────────────────────────────

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
        Mercadoria salva = mercadoriaRepo.save(mercadoria);
        return mercadoriaMapper.toResponse(salva);
    }

    @Override
    @Transactional
    public MercadoriaResponse associarMercadoria(Long empresaId, Long mercadoriaId) {
        Empresa empresa = obterEntidade(empresaId);
        Mercadoria mercadoria = mercadoriaRepo.findById(mercadoriaId)
                .orElseThrow(() -> new MercadoriaNaoEncontradaException(mercadoriaId));
        mercadoria.setEmpresa(empresa);
        Mercadoria salva = mercadoriaRepo.save(mercadoria);
        return mercadoriaMapper.toResponse(salva);
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

        if (vendaRepo.existsByMercadoriasId(mercadoriaId)) {
            throw new MercadoriaEmUsoException(mercadoriaId);
        }

        mercadoria.setEmpresa(null);
        mercadoriaRepo.save(mercadoria);
    }

    // ─── Serviços da empresa ──────────────────────────────────────────────────

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
        empresa.getServicos().add(servico);
        Servico salvo = servicoRepo.save(servico);
        return servicoMapper.toResponse(salvo);
    }

    @Override
    @Transactional
    public void removerServico(Long empresaId, Long servicoId) {
        obterEntidade(empresaId);
        Servico servico = servicoRepo.findByIdAndEmpresaId(servicoId, empresaId)
                .orElseThrow(() -> new ServicoNaoEncontradoException(servicoId));
        servicoRepo.delete(servico);
    }

    // ─── Vendas da empresa ────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<Venda> listarVendas(Long empresaId) {
        obterEntidade(empresaId);
        return vendaRepo.findByEmpresaId(empresaId);
    }

    @Override
    @Transactional(readOnly = true)
    public Venda obterVenda(Long empresaId, Long vendaId) {
        obterEntidade(empresaId);
        return vendaRepo.findByIdAndEmpresaId(vendaId, empresaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Venda " + vendaId + " não encontrada na empresa " + empresaId));
    }

    @Override
    @Transactional
    public Venda criarVenda(Long empresaId, VendaRequest request) {
        Empresa empresa = obterEntidade(empresaId);
        Venda venda = vendaService.criarVenda(request);
        venda.setEmpresa(empresa);
        empresa.getVendas().add(venda);
        return venda;
    }

    // ─── Apoio / privados ─────────────────────────────────────────────────────

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
