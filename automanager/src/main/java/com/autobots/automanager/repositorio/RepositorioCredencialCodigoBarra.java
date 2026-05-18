package com.autobots.automanager.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.autobots.automanager.entidade.CredencialCodigoBarra;
import com.autobots.automanager.entidade.Empresa;
import com.autobots.automanager.entidade.Usuario;

public interface RepositorioCredencialCodigoBarra
        extends JpaRepository<CredencialCodigoBarra, Long> {

    @Query("""
    select u from Usuario u
    join u.credenciais c
    where type(c) = CredencialCodigoBarra
    and c.codigo = :codigo
    """)
    Optional<Usuario> findByCodigoBarra(@Param("codigo") long codigo);
}