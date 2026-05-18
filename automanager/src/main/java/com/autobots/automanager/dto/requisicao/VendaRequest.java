package com.autobots.automanager.dto.requisicao;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VendaRequest {

    @NotBlank(message = "Identificação da venda é obrigatória")
    private String identificacao;

    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "Funcionário é obrigatório")
    private Long funcionarioId;

    private Long veiculoId;

    private Set<Long> mercadoriasIds;

    private Set<Long> servicosIds;

    public String getIdentificacao() { return identificacao; }
    public Long getClienteId() { return clienteId; }
    public Long getFuncionarioId() { return funcionarioId; }
    public Long getVeiculoId() { return veiculoId; }
    public Set<Long> getMercadoriasIds() { return mercadoriasIds; }
    public Set<Long> getServicosIds() { return servicosIds; }

    public void setIdentificacao(String identificacao) { this.identificacao = identificacao; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public void setFuncionarioId(Long funcionarioId) { this.funcionarioId = funcionarioId; }
    public void setVeiculoId(Long veiculoId) { this.veiculoId = veiculoId; }
    public void setMercadoriasIds(Set<Long> mercadoriasIds) { this.mercadoriasIds = mercadoriasIds; }
    public void setServicosIds(Set<Long> servicosIds) { this.servicosIds = servicosIds; }
}
