package com.autobots.automanager.excecao;

public class EmailDuplicadoException extends RuntimeException {
    public EmailDuplicadoException(String email) {
        super("E-mail já cadastrado.");
    }
}