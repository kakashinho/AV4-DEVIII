package com.autobots.automanager.entidade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import com.autobots.automanager.enumeracao.TipoVeiculo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

// @Data removido: o toString() gerado tentaria inicializar o proxy lazy de 'proprietario'.
@Getter
@Setter
@EqualsAndHashCode(exclude = { "proprietario" })
@Entity
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private TipoVeiculo tipo;
    @Column(nullable = false)
    private String modelo;
    @Column(nullable = false, unique = true)
    private String placa;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario proprietario;

    public Long getId() { return id; }
    public TipoVeiculo getTipo() { return tipo; }
    public String getModelo() { return modelo; }
    public String getPlaca() { return placa; }
    public Usuario getProprietario() { return proprietario; }

    public void setId(Long id) { this.id = id; }
    public void setTipo(TipoVeiculo tipo) { this.tipo = tipo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public void setPlaca(String placa) { this.placa = placa; }
    public void setProprietario(Usuario proprietario) { this.proprietario = proprietario; }
}
