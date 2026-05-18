package com.autobots.automanager.excecao;

public class TelefoneNaoEncontradoException extends RuntimeException {

    public TelefoneNaoEncontradoException(Long id) {
        super("Telefone não encontrado com ID: " + id);
    }
}
