package com.autobots.automanager.modelo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErroDeCampo {
    private String campo;
    private String mensagem;

    public ErroDeCampo() {}

    public ErroDeCampo(String campo, String mensagem) {
        this.campo = campo;
        this.mensagem = mensagem;
    }

    public String getCampo() { return campo; }
    public String getMensagem() { return mensagem; }

    public void setCampo(String campo) { this.campo = campo; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}
