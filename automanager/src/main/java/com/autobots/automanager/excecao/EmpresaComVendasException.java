package com.autobots.automanager.excecao;

public class EmpresaComVendasException extends RuntimeException {

    private final Long empresaId;
    private final long totalVendas;

    public EmpresaComVendasException(Long empresaId, long totalVendas) {
        super("Empresa " + empresaId + " não pode ser removida pois possui "
                + totalVendas + " venda(s) registrada(s). Vendas são histórico contábil.");
        this.empresaId = empresaId;
        this.totalVendas = totalVendas;
    }

    public Long getEmpresaId() { return empresaId; }
    public long getTotalVendas() { return totalVendas; }
}
