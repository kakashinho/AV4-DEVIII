package com.autobots.automanager.excecao;

public class ServicoEmUsoException extends RuntimeException {

    private final Long servicoId;

    public ServicoEmUsoException(Long servicoId) {
        super("Serviço " + servicoId + " está referenciado em vendas e não pode ser excluído.");
        this.servicoId = servicoId;
    }

    public Long getServicoId() { return servicoId; }
}
