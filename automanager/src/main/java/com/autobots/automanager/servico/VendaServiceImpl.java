package com.autobots.automanager.servico;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.dto.requisicao.ItemServicoRequest;
import com.autobots.automanager.dto.requisicao.ItemVendaRequest;
import com.autobots.automanager.dto.requisicao.VendaRequest;
import com.autobots.automanager.entidade.ItemServico;
import com.autobots.automanager.entidade.ItemVenda;
import com.autobots.automanager.entidade.Venda;
import com.autobots.automanager.enumeracao.PerfilUsuario;
import com.autobots.automanager.enumeracao.StatusVenda;
import com.autobots.automanager.excecao.ResourceNotFoundException;
import com.autobots.automanager.excecao.VendaNaoValidaException;
import com.autobots.automanager.modelo.ErroDeCampo;
import com.autobots.automanager.porta.MercadoriaInfo;
import com.autobots.automanager.porta.MercadoriaPort;
import com.autobots.automanager.porta.ServicoInfo;
import com.autobots.automanager.porta.ServicoPort;
import com.autobots.automanager.porta.UsuarioInfo;
import com.autobots.automanager.porta.UsuarioPort;
import com.autobots.automanager.porta.VeiculoInfo;
import com.autobots.automanager.porta.VeiculoPort;
import com.autobots.automanager.repositorio.RepositorioEmpresa;
import com.autobots.automanager.repositorio.RepositorioUsuario;
import com.autobots.automanager.repositorio.RepositorioVenda;

@Service
@Transactional
public class VendaServiceImpl implements VendaService {

    @Autowired private RepositorioVenda repositorioVenda;
    @Autowired private RepositorioEmpresa repositorioEmpresa;
    @Autowired private RepositorioUsuario repositorioUsuario;
    @Autowired private UsuarioPort usuarioPort;
    @Autowired private VeiculoPort veiculoPort;
    @Autowired private MercadoriaPort mercadoriaPort;
    @Autowired private ServicoPort servicoPort;
    @Autowired private MercadoriaService mercadoriaService;

    @Override
    @Transactional(readOnly = true)
    public List<Venda> listarVendas(Authentication authentication) {
        boolean isAdmin   = hasRole(authentication, "ROLE_ADMIN");
        boolean isGerente = hasRole(authentication, "ROLE_GERENTE");

        List<Venda> vendas;
        if (isAdmin || isGerente) {
            vendas = repositorioVenda.findAllComItens();
        } else {
            Long usuarioId = resolverUsuarioId(authentication);

            if (hasRole(authentication, "ROLE_VENDEDOR")) {
                vendas = repositorioVenda.findByFuncionarioIdComItens(usuarioId);
            } else {
                // CLIENTE — vê apenas suas próprias compras
                vendas = repositorioVenda.findByClienteIdComItens(usuarioId);
            }
        }
        vendas.forEach(v -> v.getServicos().size());
        return vendas;
    }

    private boolean hasRole(Authentication auth, String role) {
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }

    private Long resolverUsuarioId(Authentication authentication) {
        String subject = authentication.getName();
        if (subject.startsWith("CB:")) {
            long codigo = Long.parseLong(subject.substring(3));
            return repositorioUsuario.findByCredencialCodigoBarra(codigo)
                    .map(u -> u.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Usuário autenticado não encontrado para código: " + codigo));
        }
        return repositorioUsuario.findByCredencialNomeUsuario(subject)
                .map(u -> u.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário autenticado não encontrado: " + subject));
    }

    @Override
    @Transactional(readOnly = true)
    public Venda obterVenda(Long id, Authentication authentication) {
        Venda venda = repositorioVenda.findByIdComItens(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada com id: " + id));
        venda.getServicos().size();
        boolean isAdmin   = hasRole(authentication, "ROLE_ADMIN");
        boolean isGerente = hasRole(authentication, "ROLE_GERENTE");
        if (!isAdmin && !isGerente) {
            Long usuarioId = resolverUsuarioId(authentication);
            if (hasRole(authentication, "ROLE_VENDEDOR")) {
                if (!usuarioId.equals(venda.getFuncionarioId())) {
                    throw new org.springframework.security.access.AccessDeniedException(
                            "VENDEDOR só pode visualizar as próprias vendas");
                }
            } else {
                // CLIENTE
                if (!usuarioId.equals(venda.getClienteId())) {
                    throw new org.springframework.security.access.AccessDeniedException(
                            "CLIENTE só pode visualizar as próprias vendas");
                }
            }
        }
        return venda;
    }

    @Override
    public Venda criarVenda(VendaRequest request, Authentication authentication) {
        // VENDEDOR só pode registrar vendas em seu próprio nome
        if (hasRole(authentication, "ROLE_VENDEDOR")
                && !hasRole(authentication, "ROLE_ADMIN")
                && !hasRole(authentication, "ROLE_GERENTE")) {
            Long funcionarioLogadoId = resolverUsuarioId(authentication);
            if (request.getFuncionarioId() == null) {
                request.setFuncionarioId(funcionarioLogadoId);
            } else if (!funcionarioLogadoId.equals(request.getFuncionarioId())) {
                throw new org.springframework.security.access.AccessDeniedException(
                        "VENDEDOR só pode criar vendas para si mesmo. Use seu próprio ID como funcionarioId.");
            }
        }

        // ADMIN/GERENTE devem informar o funcionarioId explicitamente
        if (request.getFuncionarioId() == null) {
            throw new VendaNaoValidaException("Venda inválida",
                    List.of(new ErroDeCampo("funcionarioId", "Funcionário é obrigatório")));
        }

        List<ErroDeCampo> erros = new ArrayList<>();
        DadosVenda dados = resolverDados(request, erros);
        validarRegrasNegocio(request, dados, erros);
        if (!erros.isEmpty()) {
            throw new VendaNaoValidaException("Venda inválida", erros);
        }

        Venda venda = new Venda();
        venda.setCadastro(LocalDateTime.now());
        venda.setIdentificacao(request.getIdentificacao());
        venda.setStatus(StatusVenda.ABERTA);
        venda.setClienteId(dados.clienteId);
        venda.setClienteNomeSnapshot(dados.cliente != null ? dados.cliente.nome() : null);
        venda.setFuncionarioId(dados.funcionarioId);
        venda.setFuncionarioNomeSnapshot(dados.funcionario != null ? dados.funcionario.nome() : null);
        venda.setVeiculoId(dados.veiculoId);
        venda.setVeiculoPlacaSnapshot(dados.veiculo != null ? dados.veiculo.placa() : null);
        venda.setEmpresaId(dados.empresaId);

        BigDecimal valorTotal = BigDecimal.ZERO;
        for (ItemVendaEntry entry : dados.itensVenda) {
            ItemVenda item = new ItemVenda();
            item.setVenda(venda);
            item.setMercadoriaId(entry.mercadoria().id());
            item.setMercadoriaNomeSnapshot(entry.mercadoria().nome());
            item.setPrecoUnitarioSnapshot(entry.mercadoria().valor());
            item.setQuantidade(entry.quantidade());
            BigDecimal subtotal = entry.mercadoria().valor()
                    .multiply(BigDecimal.valueOf(entry.quantidade()));
            item.setSubtotal(subtotal);
            valorTotal = valorTotal.add(subtotal);
            venda.getItens().add(item);
        }
        for (ServicoEntry entry : dados.itensServico) {
            ItemServico item = new ItemServico();
            item.setVenda(venda);
            item.setServicoId(entry.servico().id());
            item.setServicoNomeSnapshot(entry.servico().nome());
            item.setPrecoSnapshot(entry.servico().valor());
            valorTotal = valorTotal.add(entry.servico().valor());
            venda.getServicos().add(item);
        }
        venda.setValorTotal(valorTotal);

        Venda vendaSalva = repositorioVenda.save(venda);
        abaterEstoque(dados.itensVenda);
        return vendaSalva;
    }

    @Override
    public Venda atualizarVenda(Long id, VendaRequest request, Authentication authentication) {
        Venda venda = repositorioVenda.findByIdComItens(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada com id: " + id));
        venda.getServicos().size();

        if (venda.getStatus() == StatusVenda.FECHADA || venda.getStatus() == StatusVenda.CANCELADA) {
            throw new VendaNaoValidaException(
                    "Venda com status " + venda.getStatus() + " não pode ser alterada", List.of());
        }

        // ADMIN/GERENTE devem informar o funcionarioId explicitamente
        if (request.getFuncionarioId() == null) {
            throw new VendaNaoValidaException("Venda inválida",
                    List.of(new ErroDeCampo("funcionarioId", "Funcionário é obrigatório")));
        }

        List<ErroDeCampo> erros = new ArrayList<>();
        DadosVenda dados = resolverDados(request, erros);
        validarRegrasNegocio(request, dados, erros);
        if (!erros.isEmpty()) {
            throw new VendaNaoValidaException("Venda inválida", erros);
        }

        List<ItemVendaEntry> itensAntigos = venda.getItens().stream()
                .map(i -> new ItemVendaEntry(buscarMercadoriaPorId(i.getMercadoriaId()), i.getQuantidade()))
                .toList();
        ajustarEstoqueDelta(itensAntigos, dados.itensVenda);

        venda.setIdentificacao(request.getIdentificacao());
        if (request.getStatus() != null) {
            venda.setStatus(request.getStatus());
        }
        venda.setClienteId(dados.clienteId);
        venda.setClienteNomeSnapshot(dados.cliente != null ? dados.cliente.nome() : null);
        venda.setFuncionarioId(dados.funcionarioId);
        venda.setFuncionarioNomeSnapshot(dados.funcionario != null ? dados.funcionario.nome() : null);
        venda.setVeiculoId(dados.veiculoId);
        venda.setVeiculoPlacaSnapshot(dados.veiculo != null ? dados.veiculo.placa() : null);
        venda.setEmpresaId(dados.empresaId);

        venda.getItens().clear();
        venda.getServicos().clear();

        BigDecimal valorTotal = BigDecimal.ZERO;
        for (ItemVendaEntry entry : dados.itensVenda) {
            ItemVenda item = new ItemVenda();
            item.setVenda(venda);
            item.setMercadoriaId(entry.mercadoria().id());
            item.setMercadoriaNomeSnapshot(entry.mercadoria().nome());
            item.setPrecoUnitarioSnapshot(entry.mercadoria().valor());
            item.setQuantidade(entry.quantidade());
            BigDecimal subtotal = entry.mercadoria().valor()
                    .multiply(BigDecimal.valueOf(entry.quantidade()));
            item.setSubtotal(subtotal);
            valorTotal = valorTotal.add(subtotal);
            venda.getItens().add(item);
        }
        for (ServicoEntry entry : dados.itensServico) {
            ItemServico item = new ItemServico();
            item.setVenda(venda);
            item.setServicoId(entry.servico().id());
            item.setServicoNomeSnapshot(entry.servico().nome());
            item.setPrecoSnapshot(entry.servico().valor());
            valorTotal = valorTotal.add(entry.servico().valor());
            venda.getServicos().add(item);
        }
        venda.setValorTotal(valorTotal);

        return repositorioVenda.save(venda);
    }

    @Override
    public void excluirVenda(Long id) {
        // JOIN FETCH garante que itens está na sessão — cascade ALL + orphanRemoval deleta em cascata
        Venda venda = repositorioVenda.findByIdComItens(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada com id: " + id));
        // inicializa servicos na mesma sessão para o cascade alcançar itens_servico_venda
        venda.getServicos().size();

        if (venda.getStatus() == StatusVenda.FECHADA || venda.getStatus() == StatusVenda.CANCELADA) {
            throw new VendaNaoValidaException(
                    "Venda com status " + venda.getStatus() + " não pode ser excluída", List.of());
        }

        List<ItemVendaEntry> itens = venda.getItens().stream()
                .map(i -> new ItemVendaEntry(buscarMercadoriaPorId(i.getMercadoriaId()), i.getQuantidade()))
                .toList();
        restaurarEstoque(itens);
        repositorioVenda.delete(venda);
    }

    //  Resolução de dados (acumula erros) 

    private DadosVenda resolverDados(VendaRequest request, List<ErroDeCampo> erros) {
        DadosVenda d = new DadosVenda();

        if (request.getClienteId() != null) {
            Optional<UsuarioInfo> cli = usuarioPort.buscarPorId(request.getClienteId());
            if (cli.isEmpty()) {
                erros.add(new ErroDeCampo("clienteId",
                        "Cliente " + request.getClienteId() + " não encontrado"));
            } else {
                d.clienteId = request.getClienteId();
                d.cliente = cli.get();
            }
        }

        if (request.getFuncionarioId() != null) {
            Optional<UsuarioInfo> fun = usuarioPort.buscarPorId(request.getFuncionarioId());
            if (fun.isEmpty()) {
                erros.add(new ErroDeCampo("funcionarioId",
                        "Funcionário " + request.getFuncionarioId() + " não encontrado"));
            } else {
                d.funcionarioId = request.getFuncionarioId();
                d.funcionario = fun.get();
            }
        }

        if (request.getVeiculoId() != null) {
            Optional<VeiculoInfo> v = veiculoPort.buscarPorId(request.getVeiculoId());
            if (v.isEmpty()) {
                erros.add(new ErroDeCampo("veiculoId",
                        "Veículo " + request.getVeiculoId() + " não encontrado"));
            } else {
                d.veiculoId = request.getVeiculoId();
                d.veiculo = v.get();
            }
        }

        if (request.getEmpresaId() != null) {
            if (!repositorioEmpresa.existsById(request.getEmpresaId())) {
                erros.add(new ErroDeCampo("empresaId",
                        "Empresa " + request.getEmpresaId() + " não encontrada"));
            } else {
                d.empresaId = request.getEmpresaId();
            }
        }

        if (request.getItens() != null) {
            List<Long> ids = request.getItens().stream()
                    .map(ItemVendaRequest::getMercadoriaId).toList();
            Map<Long, MercadoriaInfo> encontradas = new HashMap<>();
            mercadoriaPort.buscarPorIds(ids).forEach(m -> encontradas.put(m.id(), m));
            for (int i = 0; i < request.getItens().size(); i++) {
                ItemVendaRequest req = request.getItens().get(i);
                MercadoriaInfo m = encontradas.get(req.getMercadoriaId());
                if (m == null) {
                    erros.add(new ErroDeCampo("itens[" + i + "].mercadoriaId",
                            "Mercadoria " + req.getMercadoriaId() + " não encontrada"));
                } else {
                    d.itensVenda.add(new ItemVendaEntry(m, req.getQuantidade()));
                }
            }
        }

        if (request.getServicos() != null) {
            List<Long> ids = request.getServicos().stream()
                    .map(ItemServicoRequest::getServicoId).toList();
            Map<Long, ServicoInfo> encontrados = new HashMap<>();
            servicoPort.buscarPorIds(ids).forEach(s -> encontrados.put(s.id(), s));
            for (int i = 0; i < request.getServicos().size(); i++) {
                Long sid = request.getServicos().get(i).getServicoId();
                ServicoInfo s = encontrados.get(sid);
                if (s == null) {
                    erros.add(new ErroDeCampo("servicos[" + i + "].servicoId",
                            "Serviço " + sid + " não encontrado"));
                } else {
                    d.itensServico.add(new ServicoEntry(s));
                }
            }
        }

        return d;
    }

    //  Regras de negócio (acumuladas) 

    private void validarRegrasNegocio(VendaRequest request, DadosVenda d, List<ErroDeCampo> erros) {
        if (d.cliente != null && !d.cliente.perfis().contains(PerfilUsuario.ROLE_CLIENTE)) {
            erros.add(new ErroDeCampo("clienteId",
                    "Usuário " + d.clienteId + " não possui perfil ROLE_CLIENTE"));
        }
        if (d.funcionario != null && !d.funcionario.perfis().contains(PerfilUsuario.ROLE_VENDEDOR)) {
            erros.add(new ErroDeCampo("funcionarioId",
                    "Usuário " + d.funcionarioId + " não possui perfil ROLE_VENDEDOR"));
        }
        if (d.clienteId != null && d.funcionarioId != null && d.clienteId.equals(d.funcionarioId)) {
            erros.add(new ErroDeCampo("funcionarioId",
                    "Funcionário não pode ser o mesmo usuário que o cliente"));
        }
        if (d.veiculo != null && d.clienteId != null) {
            boolean pertenceAoCliente = d.veiculo.proprietarioId() != null
                    && d.veiculo.proprietarioId().equals(d.clienteId);
            if (!pertenceAoCliente) {
                erros.add(new ErroDeCampo("veiculoId",
                        "Veículo " + d.veiculoId + " não pertence ao cliente " + d.clienteId));
            }
        }

        for (int i = 0; i < d.itensVenda.size(); i++) {
            ItemVendaEntry entry = d.itensVenda.get(i);
            if (entry.mercadoria().quantidade() < entry.quantidade()) {
                erros.add(new ErroDeCampo("itens[" + i + "].quantidade",
                        "Estoque insuficiente para mercadoria " + entry.mercadoria().id()
                        + ". Disponível: " + entry.mercadoria().quantidade()
                        + ", solicitado: " + entry.quantidade()));
            }
        }

        boolean temItens = !d.itensVenda.isEmpty() || !d.itensServico.isEmpty();
        if (!temItens) {
            erros.add(new ErroDeCampo("itens",
                    "A venda deve conter pelo menos uma mercadoria ou serviço"));
        }
    }

    //  Estoque 

    private void abaterEstoque(List<ItemVendaEntry> itens) {
        for (ItemVendaEntry entry : itens) {
            mercadoriaService.consumirEstoque(entry.mercadoria().id(), entry.quantidade());
        }
    }

    private void restaurarEstoque(List<ItemVendaEntry> itens) {
        for (ItemVendaEntry entry : itens) {
            mercadoriaService.ajustarEstoque(entry.mercadoria().id(), entry.quantidade(), true);
        }
    }

    private void ajustarEstoqueDelta(List<ItemVendaEntry> antigas, List<ItemVendaEntry> novas) {
        Map<Long, Integer> qtdAntigas = new HashMap<>();
        antigas.forEach(e -> qtdAntigas.merge(e.mercadoria().id(), e.quantidade(), Integer::sum));
        Map<Long, Integer> qtdNovas = new HashMap<>();
        novas.forEach(e -> qtdNovas.merge(e.mercadoria().id(), e.quantidade(), Integer::sum));

        for (ItemVendaEntry nova : novas) {
            Long id = nova.mercadoria().id();
            int anterior = qtdAntigas.getOrDefault(id, 0);
            int delta = nova.quantidade() - anterior;
            if (delta > 0) {
                mercadoriaService.consumirEstoque(id, delta);
            } else if (delta < 0) {
                mercadoriaService.ajustarEstoque(id, -delta, true);
            }
        }
        for (ItemVendaEntry antiga : antigas) {
            if (!qtdNovas.containsKey(antiga.mercadoria().id())) {
                mercadoriaService.ajustarEstoque(antiga.mercadoria().id(), antiga.quantidade(), true);
            }
        }
    }

    private MercadoriaInfo buscarMercadoriaPorId(Long id) {
        return mercadoriaPort.buscarPorIds(List.of(id)).stream()
                .filter(m -> m.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Mercadoria " + id + " não encontrada"));
    }

    //  Tipos internos 

    private static class DadosVenda {
        Long clienteId;
        UsuarioInfo cliente;
        Long funcionarioId;
        UsuarioInfo funcionario;
        Long veiculoId;
        VeiculoInfo veiculo;
        Long empresaId;
        List<ItemVendaEntry> itensVenda = new ArrayList<>();
        List<ServicoEntry> itensServico = new ArrayList<>();
    }

    private record ItemVendaEntry(MercadoriaInfo mercadoria, int quantidade) {}

    private record ServicoEntry(ServicoInfo servico) {}
}
