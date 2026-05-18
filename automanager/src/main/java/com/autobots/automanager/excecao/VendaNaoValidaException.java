package com.autobots.automanager.excecao;

import java.util.Collections;
import java.util.List;

import com.autobots.automanager.modelo.ErroDeCampo;

public class VendaNaoValidaException extends RuntimeException {

    private final List<ErroDeCampo> erros;

    public VendaNaoValidaException(String mensagem) {
        super(mensagem);
        this.erros = Collections.emptyList();
    }

    public VendaNaoValidaException(String mensagem, List<ErroDeCampo> erros) {
        super(mensagem);
        this.erros = erros == null ? Collections.emptyList() : List.copyOf(erros);
    }

    public List<ErroDeCampo> getErros() {
        return erros;
    }
}
