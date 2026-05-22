package com.autobots.automanager.entidade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

// @Data removido: o toString() gerado tentaria inicializar o proxy lazy de 'empresa'.
@Getter
@Setter
@Entity
public class Mercadoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long versao;

    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa;

    @Column(nullable = false)
    private LocalDate validade;

    @Column(nullable = false)
    private LocalDate fabricacao;

    @Column(nullable = false)
    private LocalDateTime cadastro;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private long quantidade;

    @Column
    private String descricao;

    @Transient
    private boolean disponivel;

    public Long getId() { return id; }
    public Empresa getEmpresa() { return empresa; }
    public LocalDate getValidade() { return validade; }
    public LocalDate getFabricacao() { return fabricacao; }
    public LocalDateTime getCadastro() { return cadastro; }
    public String getNome() { return nome; }
    public BigDecimal getValor() { return valor; }
    public long getQuantidade() { return quantidade; }
    public String getDescricao() { return descricao; }
    public boolean isDisponivel() { return quantidade > 0; }

    public void setId(Long id) { this.id = id; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public void setValidade(LocalDate validade) { this.validade = validade; }
    public void setFabricacao(LocalDate fabricacao) { this.fabricacao = fabricacao; }
    public void setCadastro(LocalDateTime cadastro) { this.cadastro = cadastro; }
    public void setNome(String nome) { this.nome = nome; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public void setQuantidade(long quantidade) { this.quantidade = quantidade; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setDisponivel(boolean disponivel) { /* derivado de quantidade — ignorado */ }
}
