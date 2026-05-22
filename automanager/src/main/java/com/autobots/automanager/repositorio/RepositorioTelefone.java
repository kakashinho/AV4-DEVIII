package com.autobots.automanager.repositorio;

import com.autobots.automanager.entidade.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioTelefone extends JpaRepository<Telefone, Long> {

    boolean existsByDddAndNumero(String ddd, String numero);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u JOIN u.telefones t WHERE t.id = :id")
    boolean existeAssociadoAUsuario(@Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Empresa e JOIN e.telefones t WHERE t.id = :id")
    boolean existeAssociadoAEmpresa(@Param("id") Long id);
}
