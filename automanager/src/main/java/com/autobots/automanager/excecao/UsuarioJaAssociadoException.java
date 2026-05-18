package com.autobots.automanager.excecao;

public class UsuarioJaAssociadoException extends RuntimeException {

    private final Long usuarioId;
    private final Long empresaId;

    public UsuarioJaAssociadoException(Long usuarioId) {
        super("Usuário já está associado a uma empresa. ID do usuário: " + usuarioId);
        this.usuarioId = usuarioId;
        this.empresaId = null;
    }

    public UsuarioJaAssociadoException(Long usuarioId, Long empresaId) {
        super("Usuário " + usuarioId + " já está associado à empresa " + empresaId);
        this.usuarioId = usuarioId;
        this.empresaId = empresaId;
    }

    public Long getUsuarioId() { return usuarioId; }
    public Long getEmpresaId() { return empresaId; }
}