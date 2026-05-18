package com.autobots.automanager.excecao;

public class TelefoneDuplicadoException extends RuntimeException {

    public TelefoneDuplicadoException(String ddd, String numero) {
        super("Telefone já cadastrado.");
    }
}
