package com.autobots.automanager.entidade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import com.autobots.automanager.enumeracao.StatusVenda;

@Entity
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime cadastro;
    private String identificacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusVenda status = StatusVenda.ABERTA;

    // Referência por ID — sem @ManyToOne, pronto para microsserviço
    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "cliente_nome_snapshot")
    private String clienteNomeSnapshot;

    @Column(name = "funcionario_id")
    private Long funcionarioId;

    @Column(name = "funcionario_nome_snapshot")
    private String funcionarioNomeSnapshot;

    @Column(name = "veiculo_id")
    private Long veiculoId;

    @Column(name = "veiculo_placa_snapshot")
    private String veiculoPlacaSnapshot;

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    // LAZY: join fetch explícito nas queries que precisam dos itens
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemVenda> itens = new ArrayList<>();

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemServico> servicos = new ArrayList<>();

    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;

    public Long getId() { return id; }
    public LocalDateTime getCadastro() { return cadastro; }
    public String getIdentificacao() { return identificacao; }
    public StatusVenda getStatus() { return status; }
    public Long getClienteId() { return clienteId; }
    public String getClienteNomeSnapshot() { return clienteNomeSnapshot; }
    public Long getFuncionarioId() { return funcionarioId; }
    public String getFuncionarioNomeSnapshot() { return funcionarioNomeSnapshot; }
    public Long getVeiculoId() { return veiculoId; }
    public String getVeiculoPlacaSnapshot() { return veiculoPlacaSnapshot; }
    public Long getEmpresaId() { return empresaId; }
    public List<ItemVenda> getItens() { return itens; }
    public List<ItemServico> getServicos() { return servicos; }
    public BigDecimal getValorTotal() { return valorTotal; }

    public void setId(Long id) { this.id = id; }
    public void setCadastro(LocalDateTime cadastro) { this.cadastro = cadastro; }
    public void setIdentificacao(String identificacao) { this.identificacao = identificacao; }
    public void setStatus(StatusVenda status) { this.status = status; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public void setClienteNomeSnapshot(String clienteNomeSnapshot) { this.clienteNomeSnapshot = clienteNomeSnapshot; }
    public void setFuncionarioId(Long funcionarioId) { this.funcionarioId = funcionarioId; }
    public void setFuncionarioNomeSnapshot(String funcionarioNomeSnapshot) { this.funcionarioNomeSnapshot = funcionarioNomeSnapshot; }
    public void setVeiculoId(Long veiculoId) { this.veiculoId = veiculoId; }
    public void setVeiculoPlacaSnapshot(String veiculoPlacaSnapshot) { this.veiculoPlacaSnapshot = veiculoPlacaSnapshot; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
    public void setItens(List<ItemVenda> itens) { this.itens = itens; }
    public void setServicos(List<ItemServico> servicos) { this.servicos = servicos; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
}
