package com.autobots.automanager.entidade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "email", uniqueConstraints = {
    @UniqueConstraint(name = "uk_email_endereco", columnNames = "endereco")
})
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Endereço de e-mail é obrigatório")
    @jakarta.validation.constraints.Email(message = "Endereço de e-mail inválido")
    @Column(nullable = false, unique = true)
    private String endereco;

    public Long getId() { return id; }
    public String getEndereco() { return endereco; }
    public void setId(Long id) { this.id = id; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
}
