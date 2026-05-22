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
@Table(name = "itens_servico_venda")
public class ItemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;

    @Column(name = "servico_id", nullable = false)
    private Long servicoId;

    @Column(name = "servico_nome_snapshot")
    private String servicoNomeSnapshot;

    @Column(name = "preco_snapshot", precision = 10, scale = 2)
    private BigDecimal precoSnapshot;

    public Long getId() { return id; }
    public Venda getVenda() { return venda; }
    public Long getServicoId() { return servicoId; }
    public String getServicoNomeSnapshot() { return servicoNomeSnapshot; }
    public BigDecimal getPrecoSnapshot() { return precoSnapshot; }

    public void setId(Long id) { this.id = id; }
    public void setVenda(Venda venda) { this.venda = venda; }
    public void setServicoId(Long servicoId) { this.servicoId = servicoId; }
    public void setServicoNomeSnapshot(String servicoNomeSnapshot) { this.servicoNomeSnapshot = servicoNomeSnapshot; }
    public void setPrecoSnapshot(BigDecimal precoSnapshot) { this.precoSnapshot = precoSnapshot; }
}
