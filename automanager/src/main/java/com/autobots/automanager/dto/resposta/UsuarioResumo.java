package com.autobots.automanager.dto.resposta;

import java.util.Set;

import com.autobots.automanager.enumeracao.PerfilUsuario;

import lombok.Data;

@Data
public class UsuarioResumo {
    private Long id;
    private String nome;
    private String nomeSocial;
    private Set<PerfilUsuario> perfis;

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
}