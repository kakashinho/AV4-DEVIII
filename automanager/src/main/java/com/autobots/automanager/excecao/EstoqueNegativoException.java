package com.autobots.automanager.excecao;

public class EstoqueNegativoException extends RuntimeException {
    public EstoqueNegativoException(String mensagem) {
        super(mensagem);
    }
}