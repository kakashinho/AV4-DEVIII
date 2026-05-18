package com.autobots.automanager.dto.resposta;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class VendaResponse extends RepresentationModel<VendaResponse> {
    private Long id;
    private LocalDateTime cadastro;
    private String identificacao;
    private Long clienteId;
    private String clienteNome;
    private Long funcionarioId;
    private String funcionarioNome;
    private Long veiculoId;
    private String veiculoModelo;
    private Set<Long> mercadoriasIds;
    private Set<Long> servicosIds;

    public Long getId() { return id; }
    public LocalDateTime getCadastro() { return cadastro; }
    public String getIdentificacao() { return identificacao; }
    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public Long getFuncionarioId() { return funcionarioId; }
    public String getFuncionarioNome() { return funcionarioNome; }
    public Long getVeiculoId() { return veiculoId; }
    public String getVeiculoModelo() { return veiculoModelo; }
    public Set<Long> getMercadoriasIds() { return mercadoriasIds; }
    public Set<Long> getServicosIds() { return servicosIds; }

    public void setId(Long id) { this.id = id; }
    public void setCadastro(LocalDateTime cadastro) { this.cadastro = cadastro; }
    public void setIdentificacao(String identificacao) { this.identificacao = identificacao; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }
    public void setFuncionarioId(Long funcionarioId) { this.funcionarioId = funcionarioId; }
    public void setFuncionarioNome(String funcionarioNome) { this.funcionarioNome = funcionarioNome; }
    public void setVeiculoId(Long veiculoId) { this.veiculoId = veiculoId; }
    public void setVeiculoModelo(String veiculoModelo) { this.veiculoModelo = veiculoModelo; }
    public void setMercadoriasIds(Set<Long> mercadoriasIds) { this.mercadoriasIds = mercadoriasIds; }
    public void setServicosIds(Set<Long> servicosIds) { this.servicosIds = servicosIds; }
}
