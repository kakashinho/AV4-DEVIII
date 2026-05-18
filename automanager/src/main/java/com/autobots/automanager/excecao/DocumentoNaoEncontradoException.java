package com.autobots.automanager.excecao;

public class DocumentoNaoEncontradoException extends RuntimeException {

    public DocumentoNaoEncontradoException(Long id) {
        super("Documento não encontrado com ID: " + id);
    }
}
