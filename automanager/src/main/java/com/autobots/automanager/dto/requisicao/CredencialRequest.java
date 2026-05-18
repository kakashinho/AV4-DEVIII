package com.autobots.automanager.dto.requisicao;

import com.autobots.automanager.dto.validacao.CredencialValida;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@CredencialValida
public class CredencialRequest {

    private boolean inativo;

    @Size(min = 3, max = 50, message = "Nome de usuário deve ter entre 3 e 50 caracteres.")
    private String nomeUsuario;

    @Size(min = 6, max = 50, message = "Senha deve ter no mínimo 6 caracteres.")
    private String senha;

    @Min(value = 100000, message = "Código deve ter pelo menos 6 dígitos.")
    @Max(value = 999999, message = "Código deve ter no máximo 6 dígitos.")
    private Long codigo;

    public boolean isInativo() { return inativo; }
    public String getNomeUsuario() { return nomeUsuario; }
    public String getSenha() { return senha; }
    public Long getCodigo() { return codigo; }

    public void setInativo(boolean inativo) { this.inativo = inativo; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setCodigo(Long codigo) { this.codigo = codigo; }
}
