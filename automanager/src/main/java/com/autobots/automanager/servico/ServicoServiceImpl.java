package com.autobots.automanager.servico;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.dto.requisicao.ServicoRequest;
import com.autobots.automanager.dto.resposta.ServicoResponse;
import com.autobots.automanager.entidade.Servico;
import com.autobots.automanager.excecao.ServicoNaoEncontradoException;
import com.autobots.automanager.mapeador.ServicoMapper;
import com.autobots.automanager.repositorio.RepositorioServico;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicoServiceImpl implements ServicoService {

    private final RepositorioServico repositorio;
    private final ServicoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ServicoResponse> listarTodos() {

        return repositorio.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ServicoResponse buscarPorId(Long id) {

        Servico servico = obterEntidade(id);

        return mapper.toResponse(servico);
    }

    @Override
    @Transactional
    public ServicoResponse cadastrar(ServicoRequest request) {

        Servico servico = mapper.toEntity(request);

        Servico salvo = repositorio.save(servico);

        return mapper.toResponse(salvo);
    }

    @Override
    @Transactional
    public ServicoResponse atualizar(Long id, ServicoRequest request) {

        Servico servico = obterEntidade(id);

        mapper.atualizarEntidade(servico, request);

        Servico salvo = repositorio.save(servico);

        return mapper.toResponse(salvo);
    }

    @Override
    @Transactional
    public void remover(Long id) {

        Servico servico = obterEntidade(id);

        repositorio.delete(servico);
    }

    private Servico obterEntidade(Long id) {

        return repositorio.findById(id)
                .orElseThrow(() -> new ServicoNaoEncontradoException(id));
    }
}