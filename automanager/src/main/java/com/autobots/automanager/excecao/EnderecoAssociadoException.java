package com.autobots.automanager.excecao;

public class EnderecoAssociadoException extends RuntimeException {

    public EnderecoAssociadoException(Long id) {
        super("Não é possível remover o endereço pois ele está associado a um usuário ou empresa.");
    }
}
