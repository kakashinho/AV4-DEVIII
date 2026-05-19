package com.autobots.automanager.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidade.Servico;

public interface RepositorioServico extends JpaRepository<Servico, Long> {

    List<Servico> findByEmpresaId(Long empresaId);

    Optional<Servico> findByIdAndEmpresaId(Long id, Long empresaId);

    long countByEmpresaId(Long empresaId);
}
