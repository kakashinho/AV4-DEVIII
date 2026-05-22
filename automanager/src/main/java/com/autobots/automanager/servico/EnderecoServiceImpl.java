package com.autobots.automanager.servico;

import com.autobots.automanager.dto.requisicao.EnderecoRequest;
import com.autobots.automanager.dto.resposta.EnderecoResponse;
import com.autobots.automanager.entidade.Endereco;
import com.autobots.automanager.excecao.ResourceNotFoundException;
import com.autobots.automanager.mapeador.EnderecoMapper;
import com.autobots.automanager.repositorio.RepositorioEndereco;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnderecoServiceImpl implements EnderecoService {

    private final RepositorioEndereco repositorio;

    @Override
    @Transactional
    public EnderecoResponse cadastrar(EnderecoRequest request) {
        Endereco endereco = EnderecoMapper.toEntity(request);
        return EnderecoMapper.toResponse(repositorio.save(endereco));
    }

    @Override
    @Transactional(readOnly = true)
    public EnderecoResponse buscarPorId(Long id) {
        return EnderecoMapper.toResponse(obter(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoResponse> listarTodos() {
        return repositorio.findAll().stream()
                .map(EnderecoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EnderecoResponse atualizar(Long id, EnderecoRequest request) {
        Endereco endereco = obter(id);
        EnderecoMapper.aplicarUpdate(endereco, request);
        return EnderecoMapper.toResponse(repositorio.save(endereco));
    }

    @Override
    @Transactional
    public void remover(Long id) {
        repositorio.delete(obter(id));
    }

    private Endereco obter(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com id: " + id));
    }
}
