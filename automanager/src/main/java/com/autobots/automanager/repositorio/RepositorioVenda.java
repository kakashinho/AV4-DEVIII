package com.autobots.automanager.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.autobots.automanager.entidade.Venda;

public interface RepositorioVenda extends JpaRepository<Venda, Long> {

    // Queries derivadas — funcionam com campos Long diretos (empresa_id, cliente_id, etc.)
    List<Venda> findByEmpresaId(Long empresaId);
    Optional<Venda> findByIdAndEmpresaId(Long id, Long empresaId);
    long countByEmpresaId(Long empresaId);
    boolean existsByEmpresaId(Long empresaId);
    boolean existsByVeiculoId(Long veiculoId);
    boolean existsByClienteId(Long clienteId);
    boolean existsByFuncionarioId(Long funcionarioId);

    // JOIN FETCH individual — Hibernate proíbe múltiplos bags simultâneos (MultipleBagFetchException).
    // Apenas itens é carregado via JOIN FETCH; servicos é inicializado explicitamente no serviço.
    @Query("SELECT DISTINCT v FROM Venda v LEFT JOIN FETCH v.itens")
    List<Venda> findAllComItens();

    @Query("SELECT DISTINCT v FROM Venda v LEFT JOIN FETCH v.itens WHERE v.id = :id")
    Optional<Venda> findByIdComItens(@Param("id") Long id);

    @Query("SELECT DISTINCT v FROM Venda v LEFT JOIN FETCH v.itens WHERE v.empresaId = :empresaId")
    List<Venda> findByEmpresaIdComItens(@Param("empresaId") Long empresaId);

    @Query("SELECT DISTINCT v FROM Venda v LEFT JOIN FETCH v.itens WHERE v.funcionarioId = :funcionarioId")
    List<Venda> findByFuncionarioIdComItens(@Param("funcionarioId") Long funcionarioId);

    @Query("SELECT DISTINCT v FROM Venda v LEFT JOIN FETCH v.itens WHERE v.clienteId = :clienteId")
    List<Venda> findByClienteIdComItens(@Param("clienteId") Long clienteId);

    @Query("SELECT DISTINCT v FROM Venda v LEFT JOIN FETCH v.itens WHERE v.clienteId = :usuarioId OR v.funcionarioId = :usuarioId")
    List<Venda> findByUsuarioIdComItens(@Param("usuarioId") Long usuarioId);

    @Query("SELECT DISTINCT v FROM Venda v LEFT JOIN FETCH v.itens WHERE v.id = :id AND v.empresaId = :empresaId")
    Optional<Venda> findByIdAndEmpresaIdComItens(@Param("id") Long id, @Param("empresaId") Long empresaId);

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM ItemVenda i WHERE i.mercadoriaId = :mercadoriaId")
    boolean existsByItensMercadoriaId(@Param("mercadoriaId") Long mercadoriaId);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM ItemServico s WHERE s.servicoId = :servicoId")
    boolean existsByServicosServicoId(@Param("servicoId") Long servicoId);

    // Campos Long diretos após remoção dos @ManyToOne
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Venda v WHERE v.clienteId = :clienteId OR v.funcionarioId = :funcionarioId")
    boolean existsByFuncionarioIdOrClienteId(@Param("funcionarioId") Long funcionarioId, @Param("clienteId") Long clienteId);
}
