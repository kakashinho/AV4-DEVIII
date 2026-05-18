package com.autobots.automanager.servico;

import com.autobots.automanager.dto.requisicao.TelefoneRequest;
import com.autobots.automanager.entidade.Telefone;
import com.autobots.automanager.excecao.TelefoneAssociadoException;
import com.autobots.automanager.excecao.TelefoneDuplicadoException;
import com.autobots.automanager.excecao.TelefoneNaoEncontradoException;
import com.autobots.automanager.excecao.ValidationException;
import com.autobots.automanager.mapeador.TelefoneMapper;
import com.autobots.automanager.repositorio.RepositorioTelefone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelefoneServiceImpl implements TelefoneService {

    private final RepositorioTelefone repositorio;

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("O ID deve ser maior que zero.");
        }
    }

    @Override
    @Transactional
    public Telefone criar(TelefoneRequest request) {
        if (repositorio.existsByDddAndNumero(request.getDdd(), request.getNumero())) {
            throw new TelefoneDuplicadoException(request.getDdd(), request.getNumero());
        }
        return repositorio.save(TelefoneMapper.toEntity(request));
    }

    @Override
    public Telefone buscarPorId(Long id) {
        validarId(id);
        return repositorio.findById(id)
                .orElseThrow(() -> new TelefoneNaoEncontradoException(id));
    }

    @Override
    public List<Telefone> listarTodos() {
        return repositorio.findAll();
    }

    @Override
    @Transactional
    public Telefone atualizar(Long id, TelefoneRequest request) {
        Telefone telefone = buscarPorId(id);
        boolean mudou = !request.getDdd().equals(telefone.getDdd())
                || !request.getNumero().equals(telefone.getNumero());
        if (mudou && repositorio.existsByDddAndNumero(request.getDdd(), request.getNumero())) {
            throw new TelefoneDuplicadoException(request.getDdd(), request.getNumero());
        }
        telefone.setDdd(request.getDdd());
        telefone.setNumero(request.getNumero());
        return repositorio.save(telefone);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        Telefone telefone = buscarPorId(id);
        if (repositorio.existeAssociadoAUsuario(id)) {
            throw new TelefoneAssociadoException(id);
        }
        repositorio.delete(telefone);
    }
}
