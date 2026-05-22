package com.autobots.automanager.dto.requisicao;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import lombok.Data;

@Data
public class MercadoriaUpdateRequest {

    private String nome;

    private String descricao;

    @Positive(message = "Valor deve ser maior que zero")
    private BigDecimal valor;

    @FutureOrPresent(message = "Validade não pode ser anterior à data atual")
    private LocalDate validade;

    @PastOrPresent(message = "Data de fabricação não pode ser futura")
    private LocalDate fabricacao;

    // quantidade ausente intencionalmente — gerenciada via PATCH /estoque

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public BigDecimal getValor() { return valor; }
    public LocalDate getValidade() { return validade; }
    public LocalDate getFabricacao() { return fabricacao; }

    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public void setValidade(LocalDate validade) { this.validade = validade; }
    public void setFabricacao(LocalDate fabricacao) { this.fabricacao = fabricacao; }
}
