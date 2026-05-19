package com.autobots.automanager.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidade.Venda;

public interface RepositorioVenda extends JpaRepository<Venda, Long> {

    List<Venda> findByEmpresaId(Long empresaId);

    Optional<Venda> findByIdAndEmpresaId(Long id, Long empresaId);

    long countByEmpresaId(Long empresaId);

    boolean existsByEmpresaId(Long empresaId);

    boolean existsByMercadoriasId(Long mercadoriaId);
}
