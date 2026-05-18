package com.autobots.automanager.entidade;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import com.autobots.automanager.enumeracao.PerfilUsuario;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {
        "telefones", "documentos", "emails",
        "credenciais", "vendasCliente", "vendasFuncionario", "veiculos"
})
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column
    private String nomeSocial;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<PerfilUsuario> perfis = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    private Set<Mercadoria> mercadorias = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "usuario_telefone",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "telefone_id")
    )
    private Set<Telefone> telefones = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Endereco endereco;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "usuario_documento",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "documento_id")
    )
    private Set<Documento> documentos = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "usuario_email",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "email_id")
    )
    private Set<Email> emails = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "usuario_credencial",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "credencial_id")
    )
    private Set<Credencial> credenciais = new HashSet<>();

    @OneToMany(mappedBy = "cliente")
    private Set<Venda> vendasCliente = new HashSet<>();

    @OneToMany(mappedBy = "funcionario")
    private Set<Venda> vendasFuncionario = new HashSet<>();

    @OneToMany(mappedBy = "proprietario")
    private Set<Veiculo> veiculos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa;

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

    public Set<Telefone> getTelefones() {
        return telefones;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public Set<Documento> getDocumentos() {
        return documentos;
    }

    public Set<Email> getEmails() {
        return emails;
    }

    public Set<Credencial> getCredenciais() {
        return credenciais;
    }

    public Set<Venda> getVendasCliente() {
        return vendasCliente;
    }

    public Set<Venda> getVendasFuncionario() {
        return vendasFuncionario;
    }

    public Set<Veiculo> getVeiculos() {
        return veiculos;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public Set<Mercadoria> getMercadorias() {
        return mercadorias;
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

    public void setTelefones(Set<Telefone> telefones) {
        this.telefones = telefones;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public void setDocumentos(Set<Documento> documentos) {
        this.documentos = documentos;
    }

    public void setEmails(Set<Email> emails) {
        this.emails = emails;
    }

    public void setCredenciais(Set<Credencial> credenciais) {
        this.credenciais = credenciais;
    }

    public void setVendasCliente(Set<Venda> vendasCliente) {
        this.vendasCliente = vendasCliente;
    }

    public void setVendasFuncionario(Set<Venda> vendasFuncionario) {
        this.vendasFuncionario = vendasFuncionario;
    }

    public void setVeiculos(Set<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
    public void setMercadorias(Set<Mercadoria> mercadorias) {
        this.mercadorias = mercadorias;
    }
}