package com.autobots.automanager.dto.requisicao;

import java.util.Set;

import lombok.Data;

@Data
public class EmpresaUpdateRequest {

    private String razaoSocial;
    private String nomeFantasia;

    private Long enderecoId;

    private Set<Long> telefoneIds;

    public String getRazaoSocial() { return razaoSocial; }
    public String getNomeFantasia() { return nomeFantasia; }
    public Long getEnderecoId() { return enderecoId; }
    public Set<Long> getTelefoneIds() { return telefoneIds; }

    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
    public void setEnderecoId(Long enderecoId) { this.enderecoId = enderecoId; }
    public void setTelefoneIds(Set<Long> telefoneIds) { this.telefoneIds = telefoneIds; }
}
