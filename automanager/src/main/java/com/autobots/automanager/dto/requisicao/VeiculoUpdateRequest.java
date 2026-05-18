package com.autobots.automanager.dto.requisicao;

import com.autobots.automanager.enumeracao.TipoVeiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// PUT exige entidade completa (tipo, modelo, placa). Proprietário não é alterável
// por este endpoint — usar /api/veiculos/{id}/proprietario/{novoUsuarioId}.
@Data
public class VeiculoUpdateRequest {

    @NotNull(message = "Tipo é obrigatório (HATCH, SEDA, SUV, PICKUP, SW)")
    private TipoVeiculo tipo;

    @NotBlank(message = "Modelo é obrigatório")
    private String modelo;

    @NotBlank(message = "Placa é obrigatória")
    private String placa;

    public TipoVeiculo getTipo() { return tipo; }
    public String getModelo() { return modelo; }
    public String getPlaca() { return placa; }

    public void setTipo(TipoVeiculo tipo) { this.tipo = tipo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public void setPlaca(String placa) { this.placa = placa; }
}
