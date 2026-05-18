package com.autobots.automanager.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidade.Mercadoria;

public interface RepositorioMercadoria extends JpaRepository<Mercadoria, Long> {}