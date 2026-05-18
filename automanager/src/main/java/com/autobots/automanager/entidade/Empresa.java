package com.autobots.automanager.entidade;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String razaoSocial;

    @Column
    private String nomeFantasia;

    @Column(nullable = false)
    private LocalDateTime cadastro;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Telefone> telefones = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Endereco endereco;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private Set<Usuario> usuarios = new HashSet<>();

    @OneToMany(mappedBy = "empresa")
    private Set<Mercadoria> mercadorias = new HashSet<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Servico> servicos = new HashSet<>();

    @OneToMany(mappedBy = "empresa")
    private Set<Venda> vendas = new HashSet<>();

    public Long getId() { return id; }
    public String getRazaoSocial() { return razaoSocial; }
    public String getNomeFantasia() { return nomeFantasia; }
    public LocalDateTime getCadastro() { return cadastro; }
    public Set<Telefone> getTelefones() { return telefones; }
    public Endereco getEndereco() { return endereco; }
    public Set<Usuario> getUsuarios() { return usuarios; }
    public Set<Mercadoria> getMercadorias() { return mercadorias; }
    public Set<Servico> getServicos() { return servicos; }
    public Set<Venda> getVendas() { return vendas; }

    public void setId(Long id) { this.id = id; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
    public void setCadastro(LocalDateTime cadastro) { this.cadastro = cadastro; }
    public void setTelefones(Set<Telefone> telefones) { this.telefones = telefones; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }
    public void setUsuarios(Set<Usuario> usuarios) { this.usuarios = usuarios; }
    public void setMercadorias(Set<Mercadoria> mercadorias) { this.mercadorias = mercadorias; }
    public void setServicos(Set<Servico> servicos) { this.servicos = servicos; }
    public void setVendas(Set<Venda> vendas) { this.vendas = vendas; }
}
