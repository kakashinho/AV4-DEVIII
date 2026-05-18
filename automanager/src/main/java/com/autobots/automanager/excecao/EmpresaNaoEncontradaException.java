package com.autobots.automanager.excecao;

public class EmpresaNaoEncontradaException extends RuntimeException {
    public EmpresaNaoEncontradaException(Long id) {
        super("Empresa não encontrada com ID: " + id);
    }
}