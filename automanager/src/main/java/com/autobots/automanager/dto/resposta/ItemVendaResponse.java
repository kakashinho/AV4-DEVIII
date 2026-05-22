package com.autobots.automanager.dto.resposta;

import java.math.BigDecimal;

public class ItemVendaResponse {

    private Long mercadoriaId;
    private String mercadoriaNome;
    private BigDecimal precoUnitario;
    private int quantidade;
    private BigDecimal subtotal;

    public Long getMercadoriaId() { return mercadoriaId; }
    public String getMercadoriaNome() { return mercadoriaNome; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getSubtotal() { return subtotal; }

    public void setMercadoriaId(Long mercadoriaId) { this.mercadoriaId = mercadoriaId; }
    public void setMercadoriaNome(String mercadoriaNome) { this.mercadoriaNome = mercadoriaNome; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
