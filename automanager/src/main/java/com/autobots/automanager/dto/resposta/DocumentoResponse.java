package com.autobots.automanager.dto.resposta;

import java.time.LocalDate;

import com.autobots.automanager.enumeracao.TipoDocumento;

import lombok.Data;

@Data
public class DocumentoResponse {
    private Long id;
    private TipoDocumento tipo;
    private LocalDate dataEmissao;
    private String numero;

    public Long getId() { return id; }
    public TipoDocumento getTipo() { return tipo; }
    public LocalDate getDataEmissao() { return dataEmissao; }
    public String getNumero() { return numero; }

    public void setId(Long id) { this.id = id; }
    public void setTipo(TipoDocumento tipo) { this.tipo = tipo; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }
    public void setNumero(String numero) { this.numero = numero; }
}
