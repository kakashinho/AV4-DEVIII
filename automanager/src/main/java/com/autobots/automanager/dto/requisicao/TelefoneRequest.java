package com.autobots.automanager.dto.requisicao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TelefoneRequest {

    @NotBlank(message = "DDD é obrigatório.")
    @Pattern(regexp = "^\\d{2}$", message = "DDD deve conter exatamente 2 dígitos numéricos.")
    private String ddd;

    @NotBlank(message = "Número é obrigatório.")
    @Pattern(regexp = "^\\d{8,9}$", message = "Número deve conter entre 8 e 9 dígitos numéricos.")
    private String numero;

    public String getDdd() {
        return ddd;
    }

    public String getNumero() {
        return numero;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}