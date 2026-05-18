package com.autobots.automanager.dto.requisicao;

import jakarta.validation.constraints.NotNull;

public class AssociacaoCredencialRequest {

    @NotNull(message = "ID da credencial é obrigatório")
    private Long credencialId;

    public Long getCredencialId() { return credencialId; }
    public void setCredencialId(Long credencialId) { this.credencialId = credencialId; }
}
