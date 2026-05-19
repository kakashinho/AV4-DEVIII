package com.autobots.automanager.excecao;

public class MercadoriaNaoEncontradaException extends RuntimeException {

    public MercadoriaNaoEncontradaException(Long id) {
        super("Mercadoria não encontrada com id: " + id);
    }
}
