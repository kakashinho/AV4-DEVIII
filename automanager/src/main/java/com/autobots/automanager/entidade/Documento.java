package com.autobots.automanager.entidade;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import com.autobots.automanager.enumeracao.TipoDocumento;

import lombok.Data;

@Data
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "uk_documento_tipo_numero", columnNames = {"tipo", "numero"})
})
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private TipoDocumento tipo;

    @Column(nullable = false)
    private LocalDate dataEmissao;

    @Column(nullable = false)
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
