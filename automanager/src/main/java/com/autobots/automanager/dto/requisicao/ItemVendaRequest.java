package com.autobots.automanager.dto.requisicao;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ItemVendaRequest {

    @NotNull(message = "ID da mercadoria é obrigatório")
    private Long mercadoriaId;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private Integer quantidade;

    public Long getMercadoriaId() { return mercadoriaId; }
    public Integer getQuantidade() { return quantidade; }

    public void setMercadoriaId(Long mercadoriaId) { this.mercadoriaId = mercadoriaId; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
