package com.autobots.automanager.dto.requisicao;

import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;


@Data
public class EmpresaRequest {
    @NotBlank
    private String razaoSocial;
    private String nomeFantasia;

    @Valid
    private EnderecoRequest endereco;

    @Valid
    private Set<TelefoneRequest> telefones;

    private Set<Long> usuariosIds;

    private Set<Long> mercadoriasIds;
    private Set<Long> servicosIds;

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

    public Set<Long> getUsuariosIds() {
        return usuariosIds;
    }

    public Set<Long> getMercadoriasIds() {
        return mercadoriasIds;
    }

    public Set<Long> getServicosIds() {
        return servicosIds;
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

    public void setUsuariosIds(Set<Long> usuariosIds) {
        this.usuariosIds = usuariosIds;
    }

    public void setMercadoriasIds(Set<Long> mercadoriasIds) {
        this.mercadoriasIds = mercadoriasIds;
    }

    public void setServicosIds(Set<Long> servicosIds) {
        this.servicosIds = servicosIds;
    }

}