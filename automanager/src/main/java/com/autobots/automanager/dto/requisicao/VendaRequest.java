package com.autobots.automanager.dto.requisicao;

import java.util.List;

import com.autobots.automanager.enumeracao.StatusVenda;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VendaRequest {

    @NotBlank(message = "Identificação da venda é obrigatória")
    private String identificacao;

    private StatusVenda status;

    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "Funcionário é obrigatório")
    private Long funcionarioId;

    private Long veiculoId;

    private Long empresaId;

    @Valid
    private List<ItemVendaRequest> itens;

    @Valid
    private List<ItemServicoRequest> servicos;

    public String getIdentificacao() { return identificacao; }
    public StatusVenda getStatus() { return status; }
    public Long getClienteId() { return clienteId; }
    public Long getFuncionarioId() { return funcionarioId; }
    public Long getVeiculoId() { return veiculoId; }
    public Long getEmpresaId() { return empresaId; }
    public List<ItemVendaRequest> getItens() { return itens; }
    public List<ItemServicoRequest> getServicos() { return servicos; }

    public void setIdentificacao(String identificacao) { this.identificacao = identificacao; }
    public void setStatus(StatusVenda status) { this.status = status; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public void setFuncionarioId(Long funcionarioId) { this.funcionarioId = funcionarioId; }
    public void setVeiculoId(Long veiculoId) { this.veiculoId = veiculoId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
    public void setItens(List<ItemVendaRequest> itens) { this.itens = itens; }
    public void setServicos(List<ItemServicoRequest> servicos) { this.servicos = servicos; }
}
