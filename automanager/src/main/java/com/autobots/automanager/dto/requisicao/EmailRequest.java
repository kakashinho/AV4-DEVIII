package com.autobots.automanager.dto.requisicao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class EmailRequest {
    @NotBlank(message = "Endereço de e-mail é obrigatório.")
    @Email(message = "Endereço de e-mail inválido.")
    private String endereco;

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}