package com.autobots.automanager.repositorio;

import com.autobots.automanager.entidade.Endereco;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioEndereco extends JpaRepository<Endereco, Long> {

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Empresa e WHERE e.endereco.id = :id")
    boolean existeAssociadoAEmpresa(@Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE u.endereco.id = :id")
    boolean existeAssociadoAUsuario(@Param("id") Long id);
}
