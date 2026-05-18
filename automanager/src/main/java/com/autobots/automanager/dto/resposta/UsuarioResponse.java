package com.autobots.automanager.dto.resposta;

import java.util.Set;

import com.autobots.automanager.enumeracao.PerfilUsuario;

import lombok.Data;

@Data
public class UsuarioResponse {
    private Long id;
    private String nome;
    private String nomeSocial;
    private Set<PerfilUsuario> perfis;
    private EnderecoResponse endereco;
    private Set<TelefoneResponse> telefones;
    private Set<EmailResponse> emails;
    private Set<DocumentoResponse> documentos;
    private Set<CredencialResponse> credenciais;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getNomeSocial() {
        return nomeSocial;
    }

    public Set<PerfilUsuario> getPerfis() {
        return perfis;
    }

    public EnderecoResponse getEndereco() {
        return endereco;
    }

    public Set<TelefoneResponse> getTelefones() {
        return telefones;
    }

    public Set<EmailResponse> getEmails() {
        return emails;
    }

    public Set<DocumentoResponse> getDocumentos() {
        return documentos;
    }

    public Set<CredencialResponse> getCredenciais() {
        return credenciais;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNomeSocial(String nomeSocial) {
        this.nomeSocial = nomeSocial;
    }

    public void setPerfis(Set<PerfilUsuario> perfis) {
        this.perfis = perfis;
    }

    public void setEndereco(EnderecoResponse endereco) {
        this.endereco = endereco;
    }

    public void setTelefones(Set<TelefoneResponse> telefones) {
        this.telefones = telefones;
    }

    public void setEmails(Set<EmailResponse> emails) {
        this.emails = emails;
    }

    public void setDocumentos(Set<DocumentoResponse> documentos) {
        this.documentos = documentos;
    }

    public void setCredenciais(Set<CredencialResponse> credenciais) {
        this.credenciais = credenciais;
    }
}