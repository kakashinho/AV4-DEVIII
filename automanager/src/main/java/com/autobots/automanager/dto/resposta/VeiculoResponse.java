package com.autobots.automanager.dto.resposta;

import com.autobots.automanager.enumeracao.TipoVeiculo;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class VeiculoResponse extends RepresentationModel<VeiculoResponse> {
    private Long id;
    private TipoVeiculo tipo;
    private String modelo;
    private String placa;
    private Long proprietarioId;
    private String proprietarioNome;

    public Long getId() {
        return id;
    }

    public TipoVeiculo getTipo() {
        return tipo;
    }

    public String getModelo() {
        return modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public Long getProprietarioId() {
        return proprietarioId;
    }

    public String getProprietarioNome() {
        return proprietarioNome;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTipo(TipoVeiculo tipo) {
        this.tipo = tipo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setProprietarioId(Long proprietarioId) {
        this.proprietarioId = proprietarioId;
    }

    public void setProprietarioNome(String proprietarioNome) {
        this.proprietarioNome = proprietarioNome;
    }
}