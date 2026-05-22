package com.autobots.automanager.dto.requisicao;

import com.autobots.automanager.enumeracao.TipoMovimentoEstoque;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class EstoqueRequest {

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser maior que zero")
    private Long quantidade;

    @NotNull(message = "Tipo de movimento é obrigatório (ENTRADA ou SAIDA)")
    private TipoMovimentoEstoque tipo;

    public Long getQuantidade() { return quantidade; }
    public TipoMovimentoEstoque getTipo() { return tipo; }

    public void setQuantidade(Long quantidade) { this.quantidade = quantidade; }
    public void setTipo(TipoMovimentoEstoque tipo) { this.tipo = tipo; }
}
