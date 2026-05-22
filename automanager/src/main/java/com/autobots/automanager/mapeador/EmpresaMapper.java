package com.autobots.automanager.mapeador;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.autobots.automanager.dto.EmpresaResumo;
import com.autobots.automanager.dto.requisicao.EmpresaRequest;
import com.autobots.automanager.dto.requisicao.EmpresaUpdateRequest;
import com.autobots.automanager.dto.resposta.EmpresaResponse;
import com.autobots.automanager.entidade.Empresa;
import com.autobots.automanager.entidade.Endereco;
import com.autobots.automanager.entidade.Telefone;
import com.autobots.automanager.excecao.ResourceNotFoundException;
import com.autobots.automanager.repositorio.RepositorioEndereco;
import com.autobots.automanager.repositorio.RepositorioTelefone;

@Component
public class EmpresaMapper {

    private final RepositorioEndereco enderecoRepo;
    private final RepositorioTelefone telefoneRepo;

    public EmpresaMapper(RepositorioEndereco enderecoRepo, RepositorioTelefone telefoneRepo) {
        this.enderecoRepo = enderecoRepo;
        this.telefoneRepo = telefoneRepo;
    }

    public EmpresaResponse toResponse(Empresa empresa) {
        EmpresaResponse r = new EmpresaResponse();
        r.setId(empresa.getId());
        r.setRazaoSocial(empresa.getRazaoSocial());
        r.setNomeFantasia(empresa.getNomeFantasia());
        r.setCadastro(empresa.getCadastro());

        if (empresa.getEndereco() != null)
            r.setEndereco(EnderecoMapper.toResponse(empresa.getEndereco()));

        r.setTelefones(empresa.getTelefones().stream()
                .map(TelefoneMapper::toResponse).collect(Collectors.toSet()));

        return r;
    }

    public Empresa toEntity(EmpresaRequest request) {
        Empresa e = new Empresa();
        e.setRazaoSocial(request.getRazaoSocial());
        e.setNomeFantasia(request.getNomeFantasia());
        e.setCadastro(LocalDateTime.now());

        if (request.getEnderecoId() != null) {
            Endereco endereco = enderecoRepo.findById(request.getEnderecoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Endereço não encontrado com id: " + request.getEnderecoId()));
            e.setEndereco(endereco);
        }

        if (request.getTelefoneIds() != null && !request.getTelefoneIds().isEmpty()) {
            Set<Telefone> telefones = new HashSet<>(
                    telefoneRepo.findAllById(request.getTelefoneIds()));
            e.setTelefones(telefones);
        }

        return e;
    }

    public EmpresaResumo toResumo(Empresa empresa) {
        EmpresaResumo resumo = new EmpresaResumo();
        resumo.setId(empresa.getId());
        resumo.setRazaoSocial(empresa.getRazaoSocial());
        resumo.setNomeFantasia(empresa.getNomeFantasia());
        return resumo;
    }

    // Aplica apenas os campos != null do update parcial.
    // telefoneIds com null mantém o conjunto atual; lista vazia limpa todos.
    public void aplicarUpdate(Empresa empresa, EmpresaUpdateRequest request) {
        if (request.getRazaoSocial() != null) {
            empresa.setRazaoSocial(request.getRazaoSocial());
        }
        if (request.getNomeFantasia() != null) {
            empresa.setNomeFantasia(request.getNomeFantasia());
        }
        if (request.getEnderecoId() != null) {
            Endereco endereco = enderecoRepo.findById(request.getEnderecoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Endereço não encontrado com id: " + request.getEnderecoId()));
            empresa.setEndereco(endereco);
        }
        if (request.getTelefoneIds() != null) {
            empresa.getTelefones().clear();
            if (!request.getTelefoneIds().isEmpty()) {
                empresa.getTelefones().addAll(
                        telefoneRepo.findAllById(request.getTelefoneIds()));
            }
        }
    }
}
