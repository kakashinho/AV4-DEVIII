package com.autobots.automanager.entidade;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

// @Data removido: o toString() gerado tentaria inicializar o proxy lazy de 'empresa'.
@Getter
@Setter
@EqualsAndHashCode(exclude = { "empresa" })
@Entity
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa;

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public BigDecimal getValor() { return valor; }
    public String getDescricao() { return descricao; }
    public Empresa getEmpresa() { return empresa; }

    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
}
