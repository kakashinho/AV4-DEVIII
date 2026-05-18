package com.autobots.automanager.dto.requisicao;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import com.autobots.automanager.enumeracao.TipoDocumento;

import lombok.Data;

@Data
public class DocumentoRequest {

    @NotNull(message = "Tipo de documento é obrigatório")
    private TipoDocumento tipo;

    @NotBlank(message = "Número do documento é obrigatório")
    private String numero;

    @NotNull(message = "Data de emissão é obrigatória")
    @PastOrPresent(message = "Data de emissão não pode ser futura")
    private LocalDate dataEmissao;

    public TipoDocumento getTipo() { return tipo; }
    public String getNumero() { return numero; }
    public LocalDate getDataEmissao() { return dataEmissao; }

    public void setTipo(TipoDocumento tipo) { this.tipo = tipo; }
    public void setNumero(String numero) { this.numero = numero; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }
}
