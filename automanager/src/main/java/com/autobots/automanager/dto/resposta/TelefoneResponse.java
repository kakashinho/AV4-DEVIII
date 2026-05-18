package com.autobots.automanager.dto.resposta;

import lombok.Data;

@Data
public class TelefoneResponse {
    private Long id;
    private String ddd;
    private String numero;

    public Long getId() {
        return id;
    }

    public String getDdd() {
        return ddd;
    }

    public String getNumero() {
        return numero;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}