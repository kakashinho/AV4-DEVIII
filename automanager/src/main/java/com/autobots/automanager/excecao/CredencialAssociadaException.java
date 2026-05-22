package com.autobots.automanager.excecao;

public class CredencialAssociadaException extends RuntimeException {

    private final Long credencialId;

    public CredencialAssociadaException(Long credencialId) {
        super("Credencial " + credencialId + " está associada a um usuário e não pode ser excluída.");
        this.credencialId = credencialId;
    }

    public Long getCredencialId() { return credencialId; }
}
