package com.autobots.automanager.excecao;

public class MercadoriaEmUsoException extends RuntimeException {

    private final Long mercadoriaId;

    public MercadoriaEmUsoException(Long mercadoriaId) {
        super("Mercadoria " + mercadoriaId + " está referenciada em vendas e não pode ser removida.");
        this.mercadoriaId = mercadoriaId;
    }

    public Long getMercadoriaId() { return mercadoriaId; }
}
