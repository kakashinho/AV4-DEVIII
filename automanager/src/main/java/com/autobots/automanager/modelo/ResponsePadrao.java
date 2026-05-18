package com.autobots.automanager.modelo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.Link;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponsePadrao<T> {
    private int status;
    private String mensagem;
    private T dados;
    private String timestamp;
    private List<Link> links;
    private List<ErroDeCampo> erros;

    public int getStatus() {
        return status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public T getDados() {
        return dados;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<Link> getLinks() {
        return links;
    }

    public List<ErroDeCampo> getErros() {
        return erros;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void setDados(T dados) {
        this.dados = dados;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public void setErros(List<ErroDeCampo> erros) {
        this.erros = erros;
    }
}
