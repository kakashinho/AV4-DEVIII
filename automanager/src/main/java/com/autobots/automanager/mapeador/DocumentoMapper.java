package com.autobots.automanager.mapeador;

import com.autobots.automanager.dto.requisicao.DocumentoRequest;
import com.autobots.automanager.dto.resposta.DocumentoResponse;
import com.autobots.automanager.entidade.Documento;
import org.springframework.stereotype.Component;

@Component
public class DocumentoMapper {

    public static DocumentoResponse toResponse(Documento doc) {
        DocumentoResponse r = new DocumentoResponse();
        r.setId(doc.getId());
        r.setTipo(doc.getTipo());
        r.setNumero(doc.getNumero());
        r.setDataEmissao(doc.getDataEmissao());
        return r;
    }

    public static Documento toEntity(DocumentoRequest request) {
        Documento doc = new Documento();
        doc.setTipo(request.getTipo());
        doc.setNumero(request.getNumero());
        doc.setDataEmissao(request.getDataEmissao());
        return doc;
    }
}
