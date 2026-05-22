package com.autobots.automanager.excecao;

public class UsuarioComVendasException extends RuntimeException {

    private final Long usuarioId;

    public UsuarioComVendasException(Long usuarioId) {
        super("Usuário " + usuarioId + " possui vendas vinculadas e não pode ser excluído.");
        this.usuarioId = usuarioId;
    }

    public Long getUsuarioId() { return usuarioId; }
}
