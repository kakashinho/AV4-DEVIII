package com.autobots.automanager.dto.resposta;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ServicoResponse {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal valor;

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public BigDecimal getValor() { return valor; }

    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}
