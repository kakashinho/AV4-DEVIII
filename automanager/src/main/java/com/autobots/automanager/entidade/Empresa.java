package com.autobots.automanager.entidade;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

// @Data removido: o toString() gerado navegaria as coleções lazy causando LazyInitializationException.
// As 4 coleções bidirecionais (usuarios/mercadorias/servicos/vendas) foram removidas:
// cada contexto consulta o seu próprio repositório por empresaId — Empresa não precisa saber.
@Entity
@Getter
@Setter
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

    @ManyToMany
    @JoinTable(
        name = "empresa_telefone",
        joinColumns = @JoinColumn(name = "empresa_id"),
        inverseJoinColumns = @JoinColumn(name = "telefone_id")
    )
    private Set<Telefone> telefones = new HashSet<>();

    @OneToOne
    private Endereco endereco;
}
