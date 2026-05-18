package com.autobots.automanager.dto.resposta;

import lombok.Data;

@Data
public class EnderecoResponse {
    private Long id;
    private String estado;
    private String cidade;
    private String bairro;
    private String rua;
    private String numero;
    private String codigoPostal;
    private String informacoesAdicionais;

    public Long getId() {
        return id;
    }

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

    public void setId(Long id) {
        this.id = id;
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
