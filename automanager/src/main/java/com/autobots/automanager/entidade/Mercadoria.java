package com.autobots.automanager.entidade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Mercadoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Empresa empresa;

    @ManyToOne
    private Usuario usuario;

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

    private boolean disponivel;

    public Long getId() { return id; }
    public Empresa getEmpresa() { return empresa; }
    public Usuario getUsuario() { return usuario; }
    public LocalDate getValidade() { return validade; }
    public LocalDate getFabricacao() { return fabricacao; }
    public LocalDateTime getCadastro() { return cadastro; }
    public String getNome() { return nome; }
    public BigDecimal getValor() { return valor; }
    public long getQuantidade() { return quantidade; }
    public String getDescricao() { return descricao; }
    public boolean isDisponivel() { return disponivel; }

    public void setId(Long id) { this.id = id; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setValidade(LocalDate validade) { this.validade = validade; }
    public void setFabricacao(LocalDate fabricacao) { this.fabricacao = fabricacao; }
    public void setCadastro(LocalDateTime cadastro) { this.cadastro = cadastro; }
    public void setNome(String nome) { this.nome = nome; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public void setQuantidade(long quantidade) { this.quantidade = quantidade; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
}
