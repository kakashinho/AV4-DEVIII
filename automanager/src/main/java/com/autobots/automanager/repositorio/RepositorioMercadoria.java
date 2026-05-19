package com.autobots.automanager.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidade.Mercadoria;

public interface RepositorioMercadoria extends JpaRepository<Mercadoria, Long> {

    List<Mercadoria> findByEmpresaId(Long empresaId);

    long countByEmpresaId(Long empresaId);
}
