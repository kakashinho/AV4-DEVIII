package com.autobots.automanager.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.autobots.automanager.entidade.Veiculo;

public interface RepositorioVeiculo extends JpaRepository<Veiculo, Long> {

    @Query("SELECT v FROM Veiculo v LEFT JOIN FETCH v.proprietario")
    List<Veiculo> findAllComProprietario();

    @Query("SELECT v FROM Veiculo v LEFT JOIN FETCH v.proprietario WHERE v.id = :id")
    Optional<Veiculo> findByIdComProprietario(@Param("id") Long id);

    @Query("SELECT v FROM Veiculo v LEFT JOIN FETCH v.proprietario WHERE v.proprietario.id = :proprietarioId")
    List<Veiculo> findByProprietarioId(@Param("proprietarioId") Long proprietarioId);
}