package com.autobots.automanager.servico;

import com.autobots.automanager.dto.requisicao.VendaRequest;
import com.autobots.automanager.entidade.Venda;
import org.springframework.security.core.Authentication;
import java.util.List;

public interface VendaService {
    Venda criarVenda(VendaRequest request, Authentication authentication);
    List<Venda> listarVendas(Authentication authentication);
    Venda obterVenda(Long id, Authentication authentication);
    Venda atualizarVenda(Long id, VendaRequest request, Authentication authentication);
    void excluirVenda(Long id);
}
