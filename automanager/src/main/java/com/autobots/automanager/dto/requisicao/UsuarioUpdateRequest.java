package com.autobots.automanager.dto.requisicao;

import com.autobots.automanager.enumeracao.PerfilUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

public class UsuarioUpdateRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String nomeSocial;

    @NotNull(message = "Pelo menos um perfil deve ser informado")
    @Size(min = 1, message = "Pelo menos um perfil deve ser informado")
    private Set<PerfilUsuario> perfis = new HashSet<>();

    // ID de um Endereco já cadastrado. Null = manter endereço existente.
    private Long enderecoId;

    public String getNome() { return nome; }
    public String getNomeSocial() { return nomeSocial; }
    public Set<PerfilUsuario> getPerfis() { return perfis; }
    public Long getEnderecoId() { return enderecoId; }

    public void setNome(String nome) { this.nome = nome; }
    public void setNomeSocial(String nomeSocial) { this.nomeSocial = nomeSocial; }
    public void setPerfis(Set<PerfilUsuario> perfis) { this.perfis = perfis; }
    public void setEnderecoId(Long enderecoId) { this.enderecoId = enderecoId; }
}
