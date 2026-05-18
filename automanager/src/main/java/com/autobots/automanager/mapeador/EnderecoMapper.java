package com.autobots.automanager.mapeador;

import org.springframework.stereotype.Component;

import com.autobots.automanager.dto.requisicao.EnderecoRequest;
import com.autobots.automanager.dto.resposta.EnderecoResponse;
import com.autobots.automanager.entidade.Endereco;

@Component
public class EnderecoMapper {

    public static EnderecoResponse toResponse(Endereco endereco) {
        EnderecoResponse r = new EnderecoResponse();
        r.setId(endereco.getId());
        r.setEstado(endereco.getEstado());
        r.setCidade(endereco.getCidade());
        r.setBairro(endereco.getBairro());
        r.setRua(endereco.getRua());
        r.setNumero(endereco.getNumero());
        r.setCodigoPostal(endereco.getCodigoPostal());
        r.setInformacoesAdicionais(endereco.getInformacoesAdicionais());
        return r;
    }

    public static Endereco toEntity(EnderecoRequest request) {
        Endereco e = new Endereco();
        e.setEstado(request.getEstado());
        e.setCidade(request.getCidade());
        e.setBairro(request.getBairro());
        e.setRua(request.getRua());
        e.setNumero(request.getNumero());
        e.setCodigoPostal(request.getCodigoPostal());
        e.setInformacoesAdicionais(request.getInformacoesAdicionais());
        return e;
    }
}
