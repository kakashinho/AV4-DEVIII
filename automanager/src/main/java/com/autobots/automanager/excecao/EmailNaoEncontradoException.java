package com.autobots.automanager.excecao;

public class EmailNaoEncontradoException extends RuntimeException {

    public EmailNaoEncontradoException(Long id) {
        super("Email não encontrado com ID: " + id);
    }
}
