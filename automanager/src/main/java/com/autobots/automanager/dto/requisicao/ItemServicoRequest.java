package com.autobots.automanager.dto.requisicao;

import jakarta.validation.constraints.NotNull;

public class ItemServicoRequest {

    @NotNull(message = "ID do serviço é obrigatório")
    private Long servicoId;

    public Long getServicoId() { return servicoId; }
    public void setServicoId(Long servicoId) { this.servicoId = servicoId; }
}
