package com.autobots.automanager.dto.resposta;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.Data;


@Data
public class EmpresaResponse {
    private Long id;
    private String razaoSocial;
    private String nomeFantasia;
    private LocalDateTime cadastro;
    private EnderecoResponse endereco;
    private Set<TelefoneResponse> telefones;

    private long totalUsuarios;
    private long totalMercadorias;
    private long totalServicos;
    private long totalVendas;

    public Long getId() { return id; }
    public String getRazaoSocial() { return razaoSocial; }
    public String getNomeFantasia() { return nomeFantasia; }
    public LocalDateTime getCadastro() { return cadastro; }
    public EnderecoResponse getEndereco() { return endereco; }
    public Set<TelefoneResponse> getTelefones() { return telefones; }
    public long getTotalUsuarios() { return totalUsuarios; }
    public long getTotalMercadorias() { return totalMercadorias; }
    public long getTotalServicos() { return totalServicos; }
    public long getTotalVendas() { return totalVendas; }

    public void setId(Long id) { this.id = id; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
    public void setCadastro(LocalDateTime cadastro) { this.cadastro = cadastro; }
    public void setEndereco(EnderecoResponse endereco) { this.endereco = endereco; }
    public void setTelefones(Set<TelefoneResponse> telefones) { this.telefones = telefones; }
    public void setTotalUsuarios(long totalUsuarios) { this.totalUsuarios = totalUsuarios; }
    public void setTotalMercadorias(long totalMercadorias) { this.totalMercadorias = totalMercadorias; }
    public void setTotalServicos(long totalServicos) { this.totalServicos = totalServicos; }
    public void setTotalVendas(long totalVendas) { this.totalVendas = totalVendas; }
}
