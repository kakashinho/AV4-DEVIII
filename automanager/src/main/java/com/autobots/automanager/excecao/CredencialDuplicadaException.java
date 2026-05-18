package com.autobots.automanager.excecao;

public class CredencialDuplicadaException extends RuntimeException {

    public CredencialDuplicadaException() {
        super("Credencial já cadastrada.");
    }
}
