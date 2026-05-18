package com.autobots.automanager.excecao;

public class EmailAssociadoException extends RuntimeException {

    public EmailAssociadoException(Long id) {
        super("Não é possível remover o email pois ele está associado a um usuário.");
    }
}
