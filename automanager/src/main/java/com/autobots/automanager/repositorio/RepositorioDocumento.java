package com.autobots.automanager.repositorio;

import com.autobots.automanager.entidade.Documento;
import com.autobots.automanager.enumeracao.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDocumento extends JpaRepository<Documento, Long> {

    boolean existsByTipoAndNumero(TipoDocumento tipo, String numero);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u JOIN u.documentos d WHERE d.id = :id")
    boolean existeAssociadoAUsuario(@Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u JOIN u.documentos d WHERE d.id = :documentoId AND u.id <> :usuarioId")
    boolean existeAssociadoAOutroUsuario(@Param("documentoId") Long documentoId, @Param("usuarioId") Long usuarioId);
}
