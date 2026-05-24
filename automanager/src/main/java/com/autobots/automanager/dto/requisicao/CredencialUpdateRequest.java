package com.autobots.automanager.dto.requisicao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CredencialUpdateRequest {

    @NotNull(message = "Campo 'inativo' é obrigatório.")
    private Boolean inativo;

    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    private String senha;

    public Boolean getInativo() { return inativo; }
    public void setInativo(Boolean inativo) { this.inativo = inativo; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
