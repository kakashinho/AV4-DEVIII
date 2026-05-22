package com.autobots.automanager.dto.resposta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.autobots.automanager.enumeracao.StatusVenda;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = false)
public class VendaResponse extends RepresentationModel<VendaResponse> {
    private Long id;
    private LocalDateTime cadastro;
    private String identificacao;
    private StatusVenda status;
    private Long clienteId;
    private String clienteNome;
    private Long funcionarioId;
    private String funcionarioNome;
    private Long veiculoId;
    private String veiculoPlaca;
    private Long empresaId;
    private List<ItemVendaResponse> itens;
    private List<ItemServicoResponse> servicos;
    private BigDecimal valorTotal;

    public Long getId() { return id; }
    public LocalDateTime getCadastro() { return cadastro; }
    public String getIdentificacao() { return identificacao; }
    public StatusVenda getStatus() { return status; }
    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public Long getFuncionarioId() { return funcionarioId; }
    public String getFuncionarioNome() { return funcionarioNome; }
    public Long getVeiculoId() { return veiculoId; }
    public String getVeiculoPlaca() { return veiculoPlaca; }
    public Long getEmpresaId() { return empresaId; }
    public List<ItemVendaResponse> getItens() { return itens; }
    public List<ItemServicoResponse> getServicos() { return servicos; }
    public BigDecimal getValorTotal() { return valorTotal; }

    public void setId(Long id) { this.id = id; }
    public void setCadastro(LocalDateTime cadastro) { this.cadastro = cadastro; }
    public void setIdentificacao(String identificacao) { this.identificacao = identificacao; }
    public void setStatus(StatusVenda status) { this.status = status; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }
    public void setFuncionarioId(Long funcionarioId) { this.funcionarioId = funcionarioId; }
    public void setFuncionarioNome(String funcionarioNome) { this.funcionarioNome = funcionarioNome; }
    public void setVeiculoId(Long veiculoId) { this.veiculoId = veiculoId; }
    public void setVeiculoPlaca(String veiculoPlaca) { this.veiculoPlaca = veiculoPlaca; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
    public void setItens(List<ItemVendaResponse> itens) { this.itens = itens; }
    public void setServicos(List<ItemServicoResponse> servicos) { this.servicos = servicos; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
}
