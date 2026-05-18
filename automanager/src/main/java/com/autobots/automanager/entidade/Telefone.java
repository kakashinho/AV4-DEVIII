package com.autobots.automanager.entidade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.Objects;

@Entity
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ddd;

    @Column(nullable = false)
    private String numero;

    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa;

    public Long getId() { return id; }
    public String getDdd() { return ddd; }
    public String getNumero() { return numero; }

    public void setId(Long id) { this.id = id; }
    public void setDdd(String ddd) { this.ddd = ddd; }
    public void setNumero(String numero) { this.numero = numero; }

    // equals/hashCode baseado em id quando persistido, em (ddd,numero) quando transiente.
    // Isso garante comportamento correto em Set antes e após a persistência.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Telefone)) return false;
        Telefone that = (Telefone) o;
        if (id != null) return id.equals(that.id);
        return Objects.equals(ddd, that.ddd) && Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() {
        if (id != null) return Objects.hash(id);
        return Objects.hash(ddd, numero);
    }
}
