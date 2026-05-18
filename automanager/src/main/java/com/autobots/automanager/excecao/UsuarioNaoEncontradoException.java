package com.autobots.automanager.excecao;

public class UsuarioNaoEncontradoException extends RuntimeException {

    private final Long usuarioId;

    public UsuarioNaoEncontradoException(Long usuarioId) {
        super("Usuário não encontrado com ID: " + usuarioId);
        this.usuarioId = usuarioId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }
}