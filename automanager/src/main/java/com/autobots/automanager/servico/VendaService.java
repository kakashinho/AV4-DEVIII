package com.autobots.automanager.servico;

import com.autobots.automanager.dto.requisicao.VendaRequest;
import com.autobots.automanager.entidade.Venda;
import java.util.List;

public interface VendaService {
    Venda criarVenda(VendaRequest request);
    List<Venda> listarVendas();
    Venda obterVenda(Long id);
    Venda atualizarVenda(Long id, VendaRequest request);
    void excluirVenda(Long id);
}
