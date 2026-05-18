package com.autobots.automanager.dto.requisicao;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.Data;

@Data
public class MercadoriaRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String descricao;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull(message = "Validade é obrigatória")
    @FutureOrPresent(message = "Validade não pode ser anterior à data atual")
    private LocalDate validade;

    @NotNull(message = "Data de fabricação é obrigatória")
    @PastOrPresent(message = "Data de fabricação não pode ser futura")
    private LocalDate fabricacao;

    @NotNull(message = "Quantidade é obrigatória")
    @PositiveOrZero(message = "Quantidade não pode ser negativa")
    private Long quantidade;

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public BigDecimal getValor() { return valor; }
    public LocalDate getValidade() { return validade; }
    public LocalDate getFabricacao() { return fabricacao; }
    public Long getQuantidade() { return quantidade; }

    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public void setValidade(LocalDate validade) { this.validade = validade; }
    public void setFabricacao(LocalDate fabricacao) { this.fabricacao = fabricacao; }
    public void setQuantidade(Long quantidade) { this.quantidade = quantidade; }
}
