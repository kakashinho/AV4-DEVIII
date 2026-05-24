package com.autobots.automanager;

import com.autobots.automanager.entidade.*;
import com.autobots.automanager.enumeracao.PerfilUsuario;
import com.autobots.automanager.enumeracao.StatusVenda;
import com.autobots.automanager.repositorio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica que ao deletar uma Venda, os filhos ItemVenda e ItemServico
 * são removidos em cascata (CascadeType.ALL + orphanRemoval = true).
 */
@DataJpaTest
class VendaCascadeDeleteTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private RepositorioVenda repositorioVenda;

    private Long vendaId;
    private Long itemVendaId;
    private Long itemServicoId;

    @BeforeEach
    void setup() {
        // Empresa
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial("Empresa Teste");
        empresa.setNomeFantasia("Teste");
        empresa.setCadastro(LocalDateTime.now());
        em.persist(empresa);

        // Cliente (sem credencial — não é necessário para JPA puro)
        Usuario cliente = new Usuario();
        cliente.setNome("Cliente Teste");
        cliente.getPerfis().add(PerfilUsuario.ROLE_CLIENTE);
        em.persist(cliente);

        // Funcionário
        Usuario funcionario = new Usuario();
        funcionario.setNome("Vendedor Teste");
        funcionario.getPerfis().add(PerfilUsuario.ROLE_VENDEDOR);
        em.persist(funcionario);

        // Mercadoria (necessária para snapshot — não FK direta)
        Mercadoria mercadoria = new Mercadoria();
        mercadoria.setNome("Pneu");
        mercadoria.setValor(new BigDecimal("200.00"));
        mercadoria.setQuantidade(10);
        mercadoria.setFabricacao(LocalDate.now());
        mercadoria.setValidade(LocalDate.now().plusYears(2));
        mercadoria.setCadastro(LocalDateTime.now());
        mercadoria.setEmpresa(empresa);
        em.persist(mercadoria);

        // Serviço (necessário para snapshot — não FK direta)
        Servico servico = new Servico();
        servico.setNome("Balanceamento");
        servico.setValor(new BigDecimal("50.00"));
        servico.setEmpresa(empresa);
        em.persist(servico);

        // Monta a Venda com filhos
        Venda venda = new Venda();
        venda.setCadastro(LocalDateTime.now());
        venda.setIdentificacao("TESTE-001");
        venda.setStatus(StatusVenda.ABERTA);
        venda.setClienteId(cliente.getId());
        venda.setClienteNomeSnapshot(cliente.getNome());
        venda.setFuncionarioId(funcionario.getId());
        venda.setFuncionarioNomeSnapshot(funcionario.getNome());
        venda.setEmpresaId(empresa.getId());

        ItemVenda itemVenda = new ItemVenda();
        itemVenda.setVenda(venda);
        itemVenda.setMercadoriaId(mercadoria.getId());
        itemVenda.setMercadoriaNomeSnapshot(mercadoria.getNome());
        itemVenda.setPrecoUnitarioSnapshot(mercadoria.getValor());
        itemVenda.setQuantidade(2);
        itemVenda.setSubtotal(mercadoria.getValor().multiply(BigDecimal.valueOf(2)));
        venda.getItens().add(itemVenda);

        ItemServico itemServico = new ItemServico();
        itemServico.setVenda(venda);
        itemServico.setServicoId(servico.getId());
        itemServico.setServicoNomeSnapshot(servico.getNome());
        itemServico.setPrecoSnapshot(servico.getValor());
        venda.getServicos().add(itemServico);

        venda.setValorTotal(itemVenda.getSubtotal().add(servico.getValor()));

        em.persist(venda);
        em.flush();

        vendaId     = venda.getId();
        itemVendaId = itemVenda.getId();
        itemServicoId = itemServico.getId();
    }

    @Test
    void deletarVenda_deveDeletarItensEmCascata() {
        // confirma que os filhos existem antes do delete
        assertThat(em.find(ItemVenda.class, itemVendaId)).isNotNull();
        assertThat(em.find(ItemServico.class, itemServicoId)).isNotNull();

        Venda venda = em.find(Venda.class, vendaId);
        // inicializa coleções dentro da sessão (igual ao que o service faz)
        venda.getItens().size();
        venda.getServicos().size();

        repositorioVenda.delete(venda);
        em.flush();
        em.clear();

        // a própria venda deve ter sumido
        assertThat(em.find(Venda.class, vendaId)).isNull();
        // os filhos devem ter sumido junto (cascade ALL + orphanRemoval)
        assertThat(em.find(ItemVenda.class, itemVendaId)).isNull();
        assertThat(em.find(ItemServico.class, itemServicoId)).isNull();
    }

    @Test
    void removerItemDaColecao_deveDeletarFilhoPorOrphanRemoval() {
        Venda venda = em.find(Venda.class, vendaId);
        venda.getItens().size();
        venda.getServicos().size();

        // remove apenas o ItemVenda da coleção — orphanRemoval deve excluí-lo
        venda.getItens().clear();
        em.flush();
        em.clear();

        assertThat(em.find(ItemVenda.class, itemVendaId)).isNull();
        // ItemServico não foi tocado — deve continuar existindo
        assertThat(em.find(ItemServico.class, itemServicoId)).isNotNull();
        // a Venda em si continua
        assertThat(em.find(Venda.class, vendaId)).isNotNull();
    }

    @Test
    void deletarVenda_naoDeveAfetar_mercadoria_nem_servico() {
        Venda venda = em.find(Venda.class, vendaId);
        venda.getItens().size();
        venda.getServicos().size();

        // captura IDs de mercadoria/servico antes
        Long mercadoriaId = venda.getItens().get(0).getMercadoriaId();
        Long servicoId    = venda.getServicos().get(0).getServicoId();

        repositorioVenda.delete(venda);
        em.flush();
        em.clear();

        // Mercadoria e Servico são referenciados apenas por ID (sem FK) — não devem ser deletados
        assertThat(em.find(Mercadoria.class, mercadoriaId)).isNotNull();
        assertThat(em.find(Servico.class, servicoId)).isNotNull();
    }
}
