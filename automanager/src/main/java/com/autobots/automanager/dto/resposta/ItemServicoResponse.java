package com.autobots.automanager.dto.resposta;

import java.math.BigDecimal;

public class ItemServicoResponse {

    private Long servicoId;
    private String servicoNome;
    private BigDecimal preco;

    public Long getServicoId() { return servicoId; }
    public String getServicoNome() { return servicoNome; }
    public BigDecimal getPreco() { return preco; }

    public void setServicoId(Long servicoId) { this.servicoId = servicoId; }
    public void setServicoNome(String servicoNome) { this.servicoNome = servicoNome; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
}
