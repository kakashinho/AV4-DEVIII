package com.autobots.automanager.repositorio;

import com.autobots.automanager.entidade.Credencial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioCredencial extends JpaRepository<Credencial, Long> {

    @Query("select case when count(c) > 0 then true else false end from CredencialUsuarioSenha c where c.nomeUsuario = :nomeUsuario")
    boolean existsByNomeUsuario(@Param("nomeUsuario") String nomeUsuario);

    @Query("select case when count(c) > 0 then true else false end from CredencialCodigoBarra c where c.codigo = :codigo")
    boolean existsByCodigo(@Param("codigo") Long codigo);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u JOIN u.credenciais c WHERE c.id = :credencialId AND u.id <> :usuarioId")
    boolean existeAssociadoAOutroUsuario(@Param("credencialId") Long credencialId, @Param("usuarioId") Long usuarioId);
}
