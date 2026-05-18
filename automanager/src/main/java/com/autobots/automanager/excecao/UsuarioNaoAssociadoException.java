package com.autobots.automanager.excecao;

public class UsuarioNaoAssociadoException extends RuntimeException {

    private final Long usuarioId;
    private final Long empresaId;

    public UsuarioNaoAssociadoException(Long usuarioId) {
        super("Usuário não está associado a esta empresa. ID do usuário: " + usuarioId);
        this.usuarioId = usuarioId;
        this.empresaId = null;
    }

    public UsuarioNaoAssociadoException(Long usuarioId, Long empresaId) {
        super("Usuário " + usuarioId + " não está associado à empresa " + empresaId);
        this.usuarioId = usuarioId;
        this.empresaId = empresaId;
    }

    public Long getUsuarioId() { return usuarioId; }
    public Long getEmpresaId() { return empresaId; }
}