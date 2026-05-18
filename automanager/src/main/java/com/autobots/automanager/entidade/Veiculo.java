package com.autobots.automanager.entidade;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import com.autobots.automanager.enumeracao.TipoVeiculo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = { "proprietario", "vendas" })
@Entity
public class Veiculo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private TipoVeiculo tipo;
	@Column(nullable = false)
	private String modelo;
	@Column(nullable = false)
	private String placa;
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	private Usuario proprietario;
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	private Set<Venda> vendas = new HashSet<>();

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

    public Usuario getProprietario() {
        return proprietario;
    }

    public Set<Venda> getVendas() {
        return vendas;
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

    public void setProprietario(Usuario proprietario) {
        this.proprietario = proprietario;
    }

    public void setVendas(Set<Venda> vendas) {
        this.vendas = vendas;
    }
}