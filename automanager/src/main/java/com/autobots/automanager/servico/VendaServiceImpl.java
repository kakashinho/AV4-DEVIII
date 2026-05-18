package com.autobots.automanager.servico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.dto.requisicao.VendaRequest;
import com.autobots.automanager.entidade.Mercadoria;
import com.autobots.automanager.entidade.Servico;
import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.entidade.Veiculo;
import com.autobots.automanager.entidade.Venda;
import com.autobots.automanager.enumeracao.PerfilUsuario;
import com.autobots.automanager.excecao.ResourceNotFoundException;
import com.autobots.automanager.excecao.VendaNaoValidaException;
import com.autobots.automanager.modelo.ErroDeCampo;
import com.autobots.automanager.repositorio.RepositorioMercadoria;
import com.autobots.automanager.repositorio.RepositorioServico;
import com.autobots.automanager.repositorio.RepositorioUsuario;
import com.autobots.automanager.repositorio.RepositorioVeiculo;
import com.autobots.automanager.repositorio.RepositorioVenda;

@Service
@Transactional
public class VendaServiceImpl implements VendaService {

    @Autowired private RepositorioVenda repositorioVenda;
    @Autowired private RepositorioUsuario repositorioUsuario;
    @Autowired private RepositorioVeiculo repositorioVeiculo;
    @Autowired private RepositorioMercadoria repositorioMercadoria;
    @Autowired private RepositorioServico repositorioServico;

    @Override
    @Transactional(readOnly = true)
    public List<Venda> listarVendas() {
        return repositorioVenda.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Venda obterVenda(Long id) {
        return repositorioVenda.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada com id: " + id));
    }

    // Cria venda de forma atômica: valida TUDO (acumulando erros), abate estoque
    // e persiste. Qualquer falha aborta a transação inteira — nada é salvo,
    // nada é abatido.
    @Override
    public Venda criarVenda(VendaRequest request) {
        List<ErroDeCampo> erros = new ArrayList<>();
        DadosVenda dados = resolverDados(request, erros);
        validarRegrasNegocio(request, dados, erros);
        if (!erros.isEmpty()) {
            throw new VendaNaoValidaException("Venda inválida", erros);
        }

        Venda venda = new Venda();
        venda.setCadastro(LocalDateTime.now());
        venda.setIdentificacao(request.getIdentificacao());
        venda.setCliente(dados.cliente);
        venda.setFuncionario(dados.funcionario);
        venda.setVeiculo(dados.veiculo);
        venda.setMercadorias(new HashSet<>(dados.mercadorias));
        venda.setServicos(new HashSet<>(dados.servicos));

        abaterEstoque(dados.mercadorias);
        return repositorioVenda.save(venda);
    }

    // Atualiza venda revalidando TUDO e recalculando estoque pela diferença
    // entre mercadorias antigas e novas. Atômico: falha em qualquer ponto
    // reverte estoque e dados.
    @Override
    public Venda atualizarVenda(Long id, VendaRequest request) {
        Venda venda = obterVenda(id);
        List<ErroDeCampo> erros = new ArrayList<>();
        DadosVenda dados = resolverDados(request, erros);
        validarRegrasNegocio(request, dados, erros);
        if (!erros.isEmpty()) {
            throw new VendaNaoValidaException("Venda inválida", erros);
        }

        List<Mercadoria> mercadoriasAntigas = new ArrayList<>(venda.getMercadorias());
        ajustarEstoqueDelta(mercadoriasAntigas, dados.mercadorias);

        venda.setIdentificacao(request.getIdentificacao());
        venda.setCliente(dados.cliente);
        venda.setFuncionario(dados.funcionario);
        venda.setVeiculo(dados.veiculo);
        venda.getMercadorias().clear();
        venda.getMercadorias().addAll(dados.mercadorias);
        venda.getServicos().clear();
        venda.getServicos().addAll(dados.servicos);
        return repositorioVenda.save(venda);
    }

    // Restaura o estoque das mercadorias antes de remover a venda.
    @Override
    public void excluirVenda(Long id) {
        Venda venda = obterVenda(id);
        restaurarEstoque(new ArrayList<>(venda.getMercadorias()));
        repositorioVenda.delete(venda);
    }

    // ─── Resolução de IDs (acumula erros em vez de lançar no primeiro) ───────

    private DadosVenda resolverDados(VendaRequest request, List<ErroDeCampo> erros) {
        DadosVenda d = new DadosVenda();

        if (request.getClienteId() != null) {
            Optional<Usuario> cli = repositorioUsuario.findById(request.getClienteId());
            if (cli.isEmpty()) {
                erros.add(new ErroDeCampo("clienteId",
                        "Cliente " + request.getClienteId() + " não encontrado"));
            } else {
                d.cliente = cli.get();
            }
        }

        if (request.getFuncionarioId() != null) {
            Optional<Usuario> fun = repositorioUsuario.findById(request.getFuncionarioId());
            if (fun.isEmpty()) {
                erros.add(new ErroDeCampo("funcionarioId",
                        "Funcionário " + request.getFuncionarioId() + " não encontrado"));
            } else {
                d.funcionario = fun.get();
            }
        }

        if (request.getVeiculoId() != null) {
            Optional<Veiculo> v = repositorioVeiculo.findById(request.getVeiculoId());
            if (v.isEmpty()) {
                erros.add(new ErroDeCampo("veiculoId",
                        "Veículo " + request.getVeiculoId() + " não encontrado"));
            } else {
                d.veiculo = v.get();
            }
        }

        if (request.getMercadoriasIds() != null) {
            List<Long> ids = new ArrayList<>(request.getMercadoriasIds());
            Map<Long, Mercadoria> encontradas = new HashMap<>();
            repositorioMercadoria.findAllById(ids).forEach(m -> encontradas.put(m.getId(), m));
            for (int i = 0; i < ids.size(); i++) {
                Long mid = ids.get(i);
                Mercadoria m = encontradas.get(mid);
                if (m == null) {
                    erros.add(new ErroDeCampo("mercadoriasIds[" + i + "]",
                            "Mercadoria " + mid + " não encontrada"));
                } else {
                    d.mercadorias.add(m);
                }
            }
        }

        if (request.getServicosIds() != null) {
            List<Long> ids = new ArrayList<>(request.getServicosIds());
            Map<Long, Servico> encontrados = new HashMap<>();
            repositorioServico.findAllById(ids).forEach(s -> encontrados.put(s.getId(), s));
            for (int i = 0; i < ids.size(); i++) {
                Long sid = ids.get(i);
                Servico s = encontrados.get(sid);
                if (s == null) {
                    erros.add(new ErroDeCampo("servicosIds[" + i + "]",
                            "Serviço " + sid + " não encontrado"));
                } else {
                    d.servicos.add(s);
                }
            }
        }

        return d;
    }

    // ─── Regras de negócio (acumuladas) ───────────────────────────────────────

    private void validarRegrasNegocio(VendaRequest request, DadosVenda d, List<ErroDeCampo> erros) {
        if (d.cliente != null && !d.cliente.getPerfis().contains(PerfilUsuario.CLIENTE)) {
            erros.add(new ErroDeCampo("clienteId",
                    "Usuário " + d.cliente.getId() + " não possui perfil CLIENTE"));
        }
        if (d.funcionario != null && !d.funcionario.getPerfis().contains(PerfilUsuario.FUNCIONARIO)) {
            erros.add(new ErroDeCampo("funcionarioId",
                    "Usuário " + d.funcionario.getId() + " não possui perfil FUNCIONARIO"));
        }
        if (d.cliente != null && d.funcionario != null
                && d.cliente.getId().equals(d.funcionario.getId())) {
            erros.add(new ErroDeCampo("funcionarioId",
                    "Funcionário não pode ser o mesmo usuário que o cliente"));
        }
        if (d.veiculo != null && d.cliente != null) {
            boolean pertenceAoCliente = d.veiculo.getProprietario() != null
                    && d.veiculo.getProprietario().getId().equals(d.cliente.getId());
            if (!pertenceAoCliente) {
                erros.add(new ErroDeCampo("veiculoId",
                        "Veículo " + d.veiculo.getId() + " não pertence ao cliente "
                        + d.cliente.getId()));
            }
        }

        if (request.getMercadoriasIds() != null) {
            List<Long> ids = new ArrayList<>(request.getMercadoriasIds());
            for (int i = 0; i < ids.size(); i++) {
                Long mid = ids.get(i);
                Mercadoria m = d.mercadorias.stream()
                        .filter(x -> x.getId().equals(mid)).findFirst().orElse(null);
                if (m != null && m.getQuantidade() <= 0) {
                    erros.add(new ErroDeCampo("mercadoriasIds[" + i + "]",
                            "Estoque insuficiente para a mercadoria " + mid));
                }
            }
        }

        boolean temItens = !d.mercadorias.isEmpty() || !d.servicos.isEmpty();
        if (!temItens) {
            erros.add(new ErroDeCampo("itens",
                    "A venda deve conter pelo menos uma mercadoria ou serviço"));
        }
    }

    // ─── Estoque ──────────────────────────────────────────────────────────────

    // Abate 1 unidade por mercadoria vendida. Recusa se ficar negativo —
    // defensivo, já que validarRegrasNegocio teria capturado antes.
    private void abaterEstoque(List<Mercadoria> mercadorias) {
        for (Mercadoria m : mercadorias) {
            if (m.getQuantidade() <= 0) {
                throw new VendaNaoValidaException(
                        "Estoque insuficiente para mercadoria " + m.getId(),
                        List.of(new ErroDeCampo("mercadoriasIds",
                                "Estoque insuficiente para a mercadoria " + m.getId())));
            }
            m.setQuantidade(m.getQuantidade() - 1);
            m.setDisponivel(m.getQuantidade() > 0);
            repositorioMercadoria.save(m);
        }
    }

    // Devolve 1 unidade ao estoque por mercadoria restaurada.
    private void restaurarEstoque(List<Mercadoria> mercadorias) {
        for (Mercadoria m : mercadorias) {
            m.setQuantidade(m.getQuantidade() + 1);
            m.setDisponivel(true);
            repositorioMercadoria.save(m);
        }
    }

    // Calcula diferença entre o conjunto antigo e o novo da venda:
    // mercadorias removidas → devolvem ao estoque, adicionadas → abatem.
    private void ajustarEstoqueDelta(List<Mercadoria> antigas, List<Mercadoria> novas) {
        Set<Long> idsAntigas = new HashSet<>();
        antigas.forEach(m -> idsAntigas.add(m.getId()));
        Set<Long> idsNovas = new HashSet<>();
        novas.forEach(m -> idsNovas.add(m.getId()));

        List<Mercadoria> removidas = new ArrayList<>();
        for (Mercadoria m : antigas) {
            if (!idsNovas.contains(m.getId())) removidas.add(m);
        }
        List<Mercadoria> adicionadas = new ArrayList<>();
        for (Mercadoria m : novas) {
            if (!idsAntigas.contains(m.getId())) adicionadas.add(m);
        }
        restaurarEstoque(removidas);
        abaterEstoque(adicionadas);
    }

    private static class DadosVenda {
        Usuario cliente;
        Usuario funcionario;
        Veiculo veiculo;
        List<Mercadoria> mercadorias = new ArrayList<>();
        List<Servico> servicos = new ArrayList<>();
    }
}
