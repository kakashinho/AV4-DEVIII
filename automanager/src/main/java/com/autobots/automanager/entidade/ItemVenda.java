package com.autobots.automanager.entidade;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "itens_venda")
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;

    @Column(name = "mercadoria_id", nullable = false)
    private Long mercadoriaId;

    @Column(name = "mercadoria_nome_snapshot", nullable = false)
    private String mercadoriaNomeSnapshot;

    @Column(name = "preco_unitario_snapshot", precision = 10, scale = 2, nullable = false)
    private BigDecimal precoUnitarioSnapshot;

    @Column(nullable = false)
    private int quantidade;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;

    public Long getId() { return id; }
    public Venda getVenda() { return venda; }
    public Long getMercadoriaId() { return mercadoriaId; }
    public String getMercadoriaNomeSnapshot() { return mercadoriaNomeSnapshot; }
    public BigDecimal getPrecoUnitarioSnapshot() { return precoUnitarioSnapshot; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getSubtotal() { return subtotal; }

    public void setId(Long id) { this.id = id; }
    public void setVenda(Venda venda) { this.venda = venda; }
    public void setMercadoriaId(Long mercadoriaId) { this.mercadoriaId = mercadoriaId; }
    public void setMercadoriaNomeSnapshot(String mercadoriaNomeSnapshot) { this.mercadoriaNomeSnapshot = mercadoriaNomeSnapshot; }
    public void setPrecoUnitarioSnapshot(BigDecimal precoUnitarioSnapshot) { this.precoUnitarioSnapshot = precoUnitarioSnapshot; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
