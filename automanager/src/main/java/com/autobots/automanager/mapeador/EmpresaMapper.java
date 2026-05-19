package com.autobots.automanager.mapeador;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.autobots.automanager.dto.EmpresaResumo;
import com.autobots.automanager.dto.requisicao.EmpresaRequest;
import com.autobots.automanager.dto.requisicao.EmpresaUpdateRequest;
import com.autobots.automanager.dto.resposta.EmpresaResponse;
import com.autobots.automanager.entidade.Empresa;

@Component
public class EmpresaMapper {

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

        if (request.getEndereco() != null)
            e.setEndereco(EnderecoMapper.toEntity(request.getEndereco()));

        if (request.getTelefones() != null)
            e.setTelefones(request.getTelefones().stream()
                    .map(TelefoneMapper::toEntity).collect(Collectors.toSet()));
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
    // Telefones com null no payload mantêm o conjunto atual;
    // lista vazia ([]) explicitamente apaga todos os telefones.
    public void aplicarUpdate(Empresa empresa, EmpresaUpdateRequest request) {
        if (request.getRazaoSocial() != null) {
            empresa.setRazaoSocial(request.getRazaoSocial());
        }
        if (request.getNomeFantasia() != null) {
            empresa.setNomeFantasia(request.getNomeFantasia());
        }
        if (request.getEndereco() != null) {
            empresa.setEndereco(EnderecoMapper.toEntity(request.getEndereco()));
        }
        if (request.getTelefones() != null) {
            empresa.getTelefones().clear();
            empresa.getTelefones().addAll(request.getTelefones().stream()
                    .map(TelefoneMapper::toEntity).collect(Collectors.toSet()));
        }
    }
}
