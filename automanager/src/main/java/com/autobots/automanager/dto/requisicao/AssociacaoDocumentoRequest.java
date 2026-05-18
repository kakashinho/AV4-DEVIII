package com.autobots.automanager.dto.requisicao;

import jakarta.validation.constraints.NotNull;

public class AssociacaoDocumentoRequest {

    @NotNull(message = "ID do documento é obrigatório")
    private Long documentoId;

    public Long getDocumentoId() { return documentoId; }
    public void setDocumentoId(Long documentoId) { this.documentoId = documentoId; }
}
