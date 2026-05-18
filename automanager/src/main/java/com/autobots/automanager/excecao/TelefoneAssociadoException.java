package com.autobots.automanager.excecao;

public class TelefoneAssociadoException extends RuntimeException {

    public TelefoneAssociadoException(Long id) {
        super("Não é possível remover o telefone pois ele está associado a um usuário.");
    }
}
