package com.autobots.automanager.excecao;

import com.autobots.automanager.enumeracao.TipoDocumento;

public class DocumentoDuplicadoException extends RuntimeException {

    public DocumentoDuplicadoException(TipoDocumento tipo, String numero) {
        super("Documento já cadastrado.");
    }
}