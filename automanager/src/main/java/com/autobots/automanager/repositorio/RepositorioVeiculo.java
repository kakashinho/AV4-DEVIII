package com.autobots.automanager.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidade.Veiculo;

public interface RepositorioVeiculo extends JpaRepository<Veiculo, Long> {}