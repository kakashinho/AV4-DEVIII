package com.autobots.automanager.excecao;

public class ServicoInativoException extends RuntimeException {
    public ServicoInativoException(Long id) {
        super("Serviço com ID " + id + " está inativo e não pode ser utilizado.");
    }
}