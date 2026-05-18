package com.autobots.automanager.dto.resposta;

import java.time.LocalDateTime;
import java.util.Date;
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
    private Set<UsuarioReferencia> usuarios;

    public Long getId() {
        return id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public LocalDateTime getCadastro() {
        return cadastro;
    }

    public EnderecoResponse getEndereco() {
        return endereco;
    }

    public Set<TelefoneResponse> getTelefones() {
        return telefones;
    }

    public Set<UsuarioReferencia> getUsuarios() {
        return usuarios;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public void setCadastro(LocalDateTime cadastro) {
        this.cadastro = cadastro;
    }

    public void setEndereco(EnderecoResponse endereco) {
        this.endereco = endereco;
    }

    public void setTelefones(Set<TelefoneResponse> telefones) {
        this.telefones = telefones;
    }

    public void setUsuarios(Set<UsuarioReferencia> usuarios) {
        this.usuarios = usuarios;
    }
}