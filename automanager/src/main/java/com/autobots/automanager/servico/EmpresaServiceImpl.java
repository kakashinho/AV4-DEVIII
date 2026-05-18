package com.autobots.automanager.servico;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.dto.EmpresaResumo;
import com.autobots.automanager.dto.requisicao.EmpresaRequest;
import com.autobots.automanager.dto.resposta.EmpresaResponse;
import com.autobots.automanager.dto.resposta.UsuarioReferencia;
import com.autobots.automanager.entidade.Empresa;
import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.excecao.EmpresaNaoEncontradaException;
import com.autobots.automanager.excecao.UsuarioJaAssociadoException;
import com.autobots.automanager.excecao.UsuarioNaoAssociadoException;
import com.autobots.automanager.excecao.UsuarioNaoEncontradoException;
import com.autobots.automanager.mapeador.EmpresaMapper;
import com.autobots.automanager.mapeador.EnderecoMapper;
import com.autobots.automanager.mapeador.TelefoneMapper;
import com.autobots.automanager.repositorio.RepositorioEmpresa;
import com.autobots.automanager.repositorio.RepositorioUsuario;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private final RepositorioEmpresa repositorio;

    private final RepositorioUsuario usuarioRepo;

    private final EmpresaMapper mapper;

    public EmpresaServiceImpl(
            RepositorioEmpresa repositorio,
            RepositorioUsuario usuarioRepo,
            EmpresaMapper mapper) {

        this.repositorio = repositorio;
        this.usuarioRepo = usuarioRepo;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpresaResumo> listarTodas() {

        return repositorio.findAll()
                .stream()
                .map(mapper::toResumo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmpresaResponse buscarPorId(Long id) {

        Empresa empresa = obterEntidade(id);

        return mapper.toResponse(empresa);
    }

    @Override
    @Transactional
    public EmpresaResponse cadastrar(EmpresaRequest request) {

        Empresa empresa = mapper.toEntity(request);

        Empresa salva = repositorio.save(empresa);

        return mapper.toResponse(salva);
    }

    @Override
    @Transactional
    public EmpresaResponse atualizar(Long id, EmpresaRequest request) {

        Empresa empresa = obterEntidade(id);

        empresa.setRazaoSocial(request.getRazaoSocial());

        empresa.setNomeFantasia(request.getNomeFantasia());

        empresa.getTelefones().clear();

        if (request.getTelefones() != null) {

            empresa.getTelefones().addAll(
                    request.getTelefones()
                            .stream()
                            .map(TelefoneMapper::toEntity)
                            .collect(Collectors.toSet())
            );
        }

        if (request.getEndereco() != null) {

            empresa.setEndereco(
                    EnderecoMapper.toEntity(request.getEndereco())
            );
        }

        Empresa salva = repositorio.save(empresa);

        return mapper.toResponse(salva);
    }

    @Override
    @Transactional
    public void remover(Long id) {

        Empresa empresa = obterEntidade(id);

        repositorio.delete(empresa);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<UsuarioReferencia> listarUsuarios(Long empresaId) {
        Empresa empresa = obterEntidade(empresaId);
        return empresa.getUsuarios().stream()
                .map(usuario -> {
                    UsuarioReferencia ref = new UsuarioReferencia();
                    ref.setId(usuario.getId());
                    ref.setNome(usuario.getNome());
                    if (!usuario.getEmails().isEmpty()) {
                        ref.setEmail(usuario.getEmails().iterator().next().getEndereco());
                    }
                    return ref;
                })
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void associarUsuario(Long empresaId, Long usuarioId) {

        Empresa empresa = obterEntidade(empresaId);

        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException(usuarioId));

        boolean jaAssociado = empresa.getUsuarios()
                .stream()
                .anyMatch(u -> u.getId().equals(usuarioId));

        if (jaAssociado) {

            throw new UsuarioJaAssociadoException(
                    usuarioId,
                    empresaId
            );
        }

        usuario.setEmpresa(empresa);

        empresa.getUsuarios().add(usuario);

        repositorio.save(empresa);
    }

    @Override
    @Transactional
    public void desassociarUsuario(Long empresaId, Long usuarioId) {
        Empresa empresa = obterEntidade(empresaId);

        Usuario usuarioParaRemover = empresa.getUsuarios().stream()
                .filter(u -> u.getId().equals(usuarioId))
                .findFirst()
                .orElseThrow(() -> new UsuarioNaoAssociadoException(usuarioId, empresaId));

        empresa.getUsuarios().remove(usuarioParaRemover);
        usuarioParaRemover.setEmpresa(null);

        repositorio.save(empresa);
    }

    @Override
    @Transactional(readOnly = true)
    public Empresa obterEmpresa(Long id) {

        return obterEntidade(id);
    }

    private Empresa obterEntidade(Long id) {

        return repositorio.findById(id)
                .orElseThrow(() ->
                        new EmpresaNaoEncontradaException(id));
    }
}