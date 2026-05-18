package com.autobots.automanager.mapeador;

import com.autobots.automanager.dto.requisicao.CredencialRequest;
import com.autobots.automanager.dto.resposta.CredencialResponse;
import com.autobots.automanager.entidade.Credencial;
import com.autobots.automanager.entidade.CredencialCodigoBarra;
import com.autobots.automanager.entidade.CredencialUsuarioSenha;
import com.autobots.automanager.excecao.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CredencialMapper {

    public static Credencial toEntity(CredencialRequest request) {
        Credencial cred;
        if (request.getNomeUsuario() != null) {
            CredencialUsuarioSenha cus = new CredencialUsuarioSenha();
            cus.setNomeUsuario(request.getNomeUsuario());
            cus.setSenha(request.getSenha());
            cred = cus;
        } else if (request.getCodigo() != null) {
            CredencialCodigoBarra ccb = new CredencialCodigoBarra();
            ccb.setCodigo(request.getCodigo());
            cred = ccb;
        } else {
            throw new ValidationException("Credencial inválida: informe nomeUsuario+senha ou codigo");
        }
        cred.setInativo(request.isInativo());
        cred.setCriacao(LocalDateTime.now());
        return cred;
    }

    public static CredencialResponse toResponse(Credencial cred) {
        CredencialResponse r = new CredencialResponse();
        r.setId(cred.getId());
        r.setCriacao(cred.getCriacao());
        r.setUltimoAcesso(cred.getUltimoAcesso());
        r.setInativo(cred.isInativo());
        if (cred instanceof CredencialUsuarioSenha cus) {
            r.setTipo("SENHA");
            r.setNomeUsuario(cus.getNomeUsuario());
        } else if (cred instanceof CredencialCodigoBarra ccb) {
            r.setTipo("CODIGO");
            r.setCodigo(ccb.getCodigo());
        }
        return r;
    }
}
