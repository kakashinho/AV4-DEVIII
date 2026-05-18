package com.autobots.automanager.servico;

import java.util.List;
import java.util.stream.Collectors;

import com.autobots.automanager.dto.requisicao.*;
import com.autobots.automanager.dto.resposta.MercadoriaResponse;
import com.autobots.automanager.entidade.*;
import com.autobots.automanager.excecao.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.mapeador.MercadoriaMapper;
import com.autobots.automanager.repositorio.RepositorioMercadoria;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MercadoriaServiceImpl implements MercadoriaService {

    private final RepositorioMercadoria repositorio;
    private final MercadoriaMapper mapper;

    @Override
    @Transactional
    public MercadoriaResponse cadastrar(MercadoriaRequest request) {
        if (request.getQuantidade() < 0) {
            throw new EstoqueNegativoException("Quantidade inicial não pode ser negativa");
        }
        Mercadoria mercadoria = mapper.toEntity(request);
        mercadoria.setDisponivel(mercadoria.getQuantidade() > 0);
        mercadoria = repositorio.save(mercadoria);
        return mapper.toResponse(mercadoria);
    }

    @Override
    public MercadoriaResponse buscarPorId(Long id) {
        Mercadoria mercadoria = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mercadoria não encontrada"));
        return mapper.toResponse(mercadoria);
    }

    @Override
    public List<MercadoriaResponse> listarTodas() {
        return repositorio.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MercadoriaResponse atualizar(Long id, MercadoriaRequest request) {
        Mercadoria mercadoria = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mercadoria não encontrada"));

        mapper.atualizarEntidade(mercadoria, request);
        mercadoria = repositorio.save(mercadoria);
        return mapper.toResponse(mercadoria);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        Mercadoria mercadoria = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mercadoria não encontrada"));
        repositorio.delete(mercadoria);
    }

    @Override
    @Transactional
    public MercadoriaResponse ajustarEstoque(Long id, long quantidadeMovimento, boolean entrada) {
        Mercadoria mercadoria = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mercadoria não encontrada"));

        long novoEstoque = entrada ?
                mercadoria.getQuantidade() + quantidadeMovimento :
                mercadoria.getQuantidade() - quantidadeMovimento;

        if (novoEstoque < 0) {
            throw new EstoqueNegativoException(
                String.format("Operação resultaria em estoque negativo. Atual: %d, movimento: %d",
                        mercadoria.getQuantidade(), quantidadeMovimento));
        }

        mercadoria.setQuantidade(novoEstoque);
        mercadoria.setDisponivel(novoEstoque > 0);
        mercadoria = repositorio.save(mercadoria);
        return mapper.toResponse(mercadoria);
    }

    @Override
    @Transactional
    public void consumirEstoque(Long id, long quantidade) {
        Mercadoria mercadoria = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mercadoria não encontrada"));
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade a consumir deve ser positiva");
        }
        long novoEstoque = mercadoria.getQuantidade() - quantidade;
        if (novoEstoque < 0) {
            throw new EstoqueNegativoException("Estoque insuficiente para mercadoria " + id);
        }
        mercadoria.setQuantidade(novoEstoque);
        mercadoria.setDisponivel(novoEstoque > 0);
        repositorio.save(mercadoria);
    }
}