package com.autobots.automanager.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidade.Empresa;

public interface RepositorioEmpresa extends JpaRepository<Empresa, Long> {
	//public Empresa findByRazaoSocial(String nome);
}