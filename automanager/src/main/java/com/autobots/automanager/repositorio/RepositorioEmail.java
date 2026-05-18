package com.autobots.automanager.repositorio;

import com.autobots.automanager.entidade.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioEmail extends JpaRepository<Email, Long> {

    boolean existsByEndereco(String endereco);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u JOIN u.emails e WHERE e.id = :id")
    boolean existeAssociadoAUsuario(@Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u JOIN u.emails e WHERE e.id = :emailId AND u.id <> :usuarioId")
    boolean existeAssociadoAOutroUsuario(@Param("emailId") Long emailId, @Param("usuarioId") Long usuarioId);
}
