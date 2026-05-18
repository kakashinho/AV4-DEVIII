package com.autobots.automanager.dto.requisicao;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class EnderecoRequest {
    @NotBlank private String estado;
    @NotBlank private String cidade;
    @NotBlank private String bairro;
    @NotBlank private String rua;
    @NotBlank private String numero;
    @NotBlank private String codigoPostal;
    private String informacoesAdicionais;

    public String getEstado() {
        return estado;
    }

    public String getCidade() {
        return cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public String getRua() {
        return rua;
    }

    public String getNumero() {
        return numero;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public String getInformacoesAdicionais() {
        return informacoesAdicionais;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public void setInformacoesAdicionais(String informacoesAdicionais) {
        this.informacoesAdicionais = informacoesAdicionais;
    }
}