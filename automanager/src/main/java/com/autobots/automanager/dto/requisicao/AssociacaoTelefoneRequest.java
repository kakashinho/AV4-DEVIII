package com.autobots.automanager.dto.requisicao;

import jakarta.validation.constraints.NotNull;

public class AssociacaoTelefoneRequest {

    @NotNull(message = "ID do telefone é obrigatório")
    private Long telefoneId;

    public Long getTelefoneId() { return telefoneId; }
    public void setTelefoneId(Long telefoneId) { this.telefoneId = telefoneId; }
}
