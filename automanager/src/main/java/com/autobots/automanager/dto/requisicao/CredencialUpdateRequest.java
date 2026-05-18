package com.autobots.automanager.dto.requisicao;

import jakarta.validation.constraints.NotNull;

public class CredencialUpdateRequest {

    @NotNull(message = "Campo 'inativo' é obrigatório.")
    private Boolean inativo;

    public Boolean getInativo() { return inativo; }
    public void setInativo(Boolean inativo) { this.inativo = inativo; }
}
