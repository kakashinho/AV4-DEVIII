package com.autobots.automanager.excecao;

public class CredencialNaoEncontradaException extends RuntimeException {

    public CredencialNaoEncontradaException(Long id) {
        super("Credencial não encontrada com ID: " + id);
    }
}
