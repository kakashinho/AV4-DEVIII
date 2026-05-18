package com.autobots.automanager.excecao;

public class DocumentoAssociadoException extends RuntimeException {

    public DocumentoAssociadoException(Long id) {
        super("Não é possível remover o documento pois ele está associado a um usuário.");
    }
}
