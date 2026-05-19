package com.autobots.automanager.dto.requisicao;

import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;


@Data
public class EmpresaRequest {

    @NotBlank(message = "Razão social é obrigatória")
    private String razaoSocial;

    private String nomeFantasia;

    @Valid
    private EnderecoRequest endereco;

    @Valid
    private Set<TelefoneRequest> telefones;

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public EnderecoRequest getEndereco() {
        return endereco;
    }

    public Set<TelefoneRequest> getTelefones() {
        return telefones;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public void setEndereco(EnderecoRequest endereco) {
        this.endereco = endereco;
    }

    public void setTelefones(Set<TelefoneRequest> telefones) {
        this.telefones = telefones;
    }
}
