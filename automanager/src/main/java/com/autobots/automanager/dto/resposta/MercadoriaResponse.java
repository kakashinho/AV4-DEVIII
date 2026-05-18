package com.autobots.automanager.dto.resposta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MercadoriaResponse {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal valor;
    private long quantidadeEstoque;
    private boolean disponivel;
    private LocalDate validade;
    private LocalDate fabricacao;
    private LocalDateTime cadastro;

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public BigDecimal getValor() { return valor; }
    public long getQuantidadeEstoque() { return quantidadeEstoque; }
    public boolean isDisponivel() { return disponivel; }
    public LocalDate getValidade() { return validade; }
    public LocalDate getFabricacao() { return fabricacao; }
    public LocalDateTime getCadastro() { return cadastro; }

    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public void setQuantidadeEstoque(long quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
    public void setValidade(LocalDate validade) { this.validade = validade; }
    public void setFabricacao(LocalDate fabricacao) { this.fabricacao = fabricacao; }
    public void setCadastro(LocalDateTime cadastro) { this.cadastro = cadastro; }
}
