package com.autobots.automanager.dto.requisicao;

import jakarta.validation.constraints.NotNull;

public class AssociacaoEmailRequest {

    @NotNull(message = "ID do email é obrigatório")
    private Long emailId;

    public Long getEmailId() { return emailId; }
    public void setEmailId(Long emailId) { this.emailId = emailId; }
}
