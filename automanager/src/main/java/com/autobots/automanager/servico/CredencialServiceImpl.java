package com.autobots.automanager.servico;

import com.autobots.automanager.dto.requisicao.CredencialRequest;
import com.autobots.automanager.dto.requisicao.CredencialUpdateRequest;
import com.autobots.automanager.entidade.Credencial;
import com.autobots.automanager.entidade.CredencialUsuarioSenha;
import com.autobots.automanager.excecao.CredencialDuplicadaException;
import com.autobots.automanager.excecao.CredencialNaoEncontradaException;
import com.autobots.automanager.excecao.ValidationException;
import com.autobots.automanager.mapeador.CredencialMapper;
import com.autobots.automanager.repositorio.RepositorioCredencial;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CredencialServiceImpl implements CredencialService {

    private final RepositorioCredencial repositorio;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("O ID deve ser maior que zero.");
        }
    }

    @Override
    @Transactional
    public Credencial criar(CredencialRequest request) {
        if (request.getNomeUsuario() != null && repositorio.existsByNomeUsuario(request.getNomeUsuario().trim())) {
            throw new CredencialDuplicadaException();
        }
        if (request.getCodigo() != null && repositorio.existsByCodigo(request.getCodigo())) {
            throw new CredencialDuplicadaException();
        }
        Credencial cred = CredencialMapper.toEntity(request);
        if (cred instanceof CredencialUsuarioSenha cus && cus.getSenha() != null) {
            cus.setSenha(passwordEncoder.encode(cus.getSenha()));
        }
        return repositorio.save(cred);
    }

    @Override
    public Credencial buscarPorId(Long id) {
        validarId(id);
        return repositorio.findById(id)
                .orElseThrow(() -> new CredencialNaoEncontradaException(id));
    }

    @Override
    public List<Credencial> listarTodos() {
        return repositorio.findAll();
    }

    @Override
    @Transactional
    public Credencial atualizar(Long id, CredencialUpdateRequest request) {
        Credencial cred = buscarPorId(id);
        cred.setInativo(request.getInativo());
        return repositorio.save(cred);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        repositorio.delete(buscarPorId(id));
    }
}
