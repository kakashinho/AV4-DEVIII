package com.autobots.automanager.dto.resposta;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CredencialResponse {
    private Long id;
    private LocalDateTime criacao;
    private LocalDateTime ultimoAcesso;
    private boolean inativo;
    private String tipo;
    private String nomeUsuario;
    private Long codigo;

    public Long getId() { return id; }
    public LocalDateTime getCriacao() { return criacao; }
    public LocalDateTime getUltimoAcesso() { return ultimoAcesso; }
    public boolean isInativo() { return inativo; }
    public String getTipo() { return tipo; }
    public String getNomeUsuario() { return nomeUsuario; }
    public Long getCodigo() { return codigo; }

    public void setId(Long id) { this.id = id; }
    public void setCriacao(LocalDateTime criacao) { this.criacao = criacao; }
    public void setUltimoAcesso(LocalDateTime ultimoAcesso) { this.ultimoAcesso = ultimoAcesso; }
    public void setInativo(boolean inativo) { this.inativo = inativo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    public void setCodigo(Long codigo) { this.codigo = codigo; }
}
