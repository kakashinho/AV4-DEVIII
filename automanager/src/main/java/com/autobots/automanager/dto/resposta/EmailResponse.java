package com.autobots.automanager.dto.resposta;

import lombok.Data;

@Data
public class EmailResponse {
    private Long id;
    private String endereco;

    public Long getId() {
        return id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}