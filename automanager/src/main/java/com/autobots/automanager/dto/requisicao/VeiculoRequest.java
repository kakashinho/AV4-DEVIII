package com.autobots.automanager.dto.requisicao;

import com.autobots.automanager.enumeracao.TipoVeiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VeiculoRequest {

    @NotNull(message = "Tipo é obrigatório (HATCH, SEDA, SUV, PICKUP, SW)")
    private TipoVeiculo tipo;

    @NotBlank(message = "Modelo é obrigatório")
    private String modelo;

    @NotBlank(message = "Placa é obrigatória")
    private String placa;

    @NotNull(message = "Proprietário é obrigatório na criação do veículo")
    private Long proprietarioId;

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
}