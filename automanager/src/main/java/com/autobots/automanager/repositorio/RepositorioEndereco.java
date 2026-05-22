package com.autobots.automanager.repositorio;

import com.autobots.automanager.entidade.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioEndereco extends JpaRepository<Endereco, Long> {
}
