package com.autobots.automanager.entidade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class CredencialCodigoBarra extends Credencial {
	@Column(nullable = false, unique = true)
	private long codigo;

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }
}