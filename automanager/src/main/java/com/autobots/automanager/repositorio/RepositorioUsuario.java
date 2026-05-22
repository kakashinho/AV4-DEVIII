package com.autobots.automanager.repositorio;

import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.enumeracao.PerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByDocumentosNumero(String numeroDocumento);
    Optional<Usuario> findByEmailsEndereco(String email);

    @Query("""
        select u from Usuario u
        join u.credenciais c
        where type(c) = CredencialCodigoBarra
        and c.codigo = :codigo
    """)
    Optional<Usuario> findByCredencialCodigoBarra(@Param("codigo") Long codigo);

    boolean existsByDocumentosNumero(String numeroDocumento);
    boolean existsByEmailsEndereco(String email);
    boolean existsByTelefonesDddAndTelefonesNumero(String ddd, String numero);

    List<Usuario> findByEmpresaId(Long empresaId);

    List<Usuario> findByEmpresaIdAndPerfisContaining(Long empresaId, PerfilUsuario perfil);

    Optional<Usuario> findByIdAndEmpresaId(Long id, Long empresaId);

    long countByEmpresaId(Long empresaId);

    @Query("""
        select case when count(u) > 0 then true else false end
        from Usuario u
        join u.credenciais c
        where type(c) = CredencialCodigoBarra
        and c.codigo = :codigo
    """)
    boolean existsByCredencialCodigoBarra(@Param("codigo") Long codigo);

    @Query("""
        select case when count(u) > 0 then true else false end
        from Usuario u
        join u.credenciais c
        where type(c) = CredencialUsuarioSenha
        and c.nomeUsuario = :nomeUsuario
    """)
    boolean existsByCredencialNomeUsuario(@Param("nomeUsuario") String nomeUsuario);

    @Query("""
        select u from Usuario u
        join fetch u.credenciais c
        where type(c) = CredencialUsuarioSenha
        and c.nomeUsuario = :nomeUsuario
    """)
    Optional<Usuario> findByCredencialNomeUsuario(@Param("nomeUsuario") String nomeUsuario);
}
