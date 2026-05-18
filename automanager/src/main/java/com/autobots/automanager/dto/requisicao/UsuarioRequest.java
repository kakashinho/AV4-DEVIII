package com.autobots.automanager.dto.requisicao;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import com.autobots.automanager.enumeracao.PerfilUsuario;

@Data
public class UsuarioRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String nomeSocial;

    @NotNull
    @Size(min = 1, message = "Pelo menos um perfil deve ser informado")
    private Set<PerfilUsuario> perfis = new HashSet<>();

    @NotNull(message = "Endereço é obrigatório")
    @Valid
    private EnderecoRequest endereco;

    public String getNome() { return nome; }
    public String getNomeSocial() { return nomeSocial; }
    public Set<PerfilUsuario> getPerfis() { return perfis; }
    public EnderecoRequest getEndereco() { return endereco; }

    public void setNome(String nome) { this.nome = nome; }
    public void setNomeSocial(String nomeSocial) { this.nomeSocial = nomeSocial; }
    public void setPerfis(Set<PerfilUsuario> perfis) { this.perfis = perfis; }
    public void setEndereco(EnderecoRequest endereco) { this.endereco = endereco; }
}
