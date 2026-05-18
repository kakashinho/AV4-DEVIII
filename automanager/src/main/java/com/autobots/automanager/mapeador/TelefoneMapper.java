package com.autobots.automanager.mapeador;

import org.springframework.stereotype.Component;

import com.autobots.automanager.dto.requisicao.TelefoneRequest;
import com.autobots.automanager.dto.resposta.TelefoneResponse;
import com.autobots.automanager.entidade.Telefone;

@Component
public class TelefoneMapper {

    public static TelefoneResponse toResponse(Telefone telefone) {
        TelefoneResponse r = new TelefoneResponse();
        r.setId(telefone.getId());
        r.setDdd(telefone.getDdd());
        r.setNumero(telefone.getNumero());
        return r;
    }

    public static Telefone toEntity(TelefoneRequest request) {
        Telefone t = new Telefone();
        t.setDdd(request.getDdd());
        t.setNumero(request.getNumero());
        return t;
    }
}
