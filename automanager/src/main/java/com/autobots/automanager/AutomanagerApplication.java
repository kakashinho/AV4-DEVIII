package com.autobots.automanager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.entidade.*;
import com.autobots.automanager.enumeracao.*;
import com.autobots.automanager.repositorio.*;

@SpringBootApplication
public class AutomanagerApplication implements CommandLineRunner {

	@Autowired private RepositorioEmpresa repositorioEmpresa;
	@Autowired private RepositorioEndereco repositorioEndereco;
	@Autowired private RepositorioTelefone repositorioTelefone;
	@Autowired private RepositorioEmail repositorioEmail;
	@Autowired private RepositorioUsuario repositorioUsuario;
	@Autowired private RepositorioCredencial repositorioCredencial;
	@Autowired private RepositorioMercadoria repositorioMercadoria;
	@Autowired private RepositorioServico repositorioServico;
	@Autowired private RepositorioVenda repositorioVenda;
	@Autowired private RepositorioVeiculo repositorioVeiculo;

	public static void main(String[] args) {
		SpringApplication.run(AutomanagerApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		// Guard: evita re-execução em banco persistente
		if (repositorioEmpresa.count() > 0) {
			return;
		}

		BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

		// ── Usuário administrador (login: admin / 123456) ──────────────────────
		CredencialUsuarioSenha credencialAdmin = new CredencialUsuarioSenha();
		credencialAdmin.setNomeUsuario("admin");
		credencialAdmin.setSenha(codificador.encode("123456"));
		credencialAdmin.setCriacao(LocalDateTime.now());
		credencialAdmin.setInativo(false);
		credencialAdmin = (CredencialUsuarioSenha) repositorioCredencial.save(credencialAdmin);

		Usuario admin = new Usuario();
		admin.setNome("Administrador");
		admin.getPerfis().add(PerfilUsuario.ROLE_ADMIN);
		admin.getCredenciais().add(credencialAdmin);
		repositorioUsuario.save(admin);

		// ── Usuário gerente (login: gerente / 123456) ──────────────────────────
		CredencialUsuarioSenha credencialGerente = new CredencialUsuarioSenha();
		credencialGerente.setNomeUsuario("gerente");
		credencialGerente.setSenha(codificador.encode("123456"));
		credencialGerente.setCriacao(LocalDateTime.now());
		credencialGerente.setInativo(false);
		credencialGerente = (CredencialUsuarioSenha) repositorioCredencial.save(credencialGerente);

		Usuario gerente = new Usuario();
		gerente.setNome("Gerente");
		gerente.getPerfis().add(PerfilUsuario.ROLE_GERENTE);
		gerente.getCredenciais().add(credencialGerente);
		repositorioUsuario.save(gerente);

		Endereco enderecoEmpresa = new Endereco();
		enderecoEmpresa.setEstado("São Paulo");
		enderecoEmpresa.setCidade("São Paulo");
		enderecoEmpresa.setBairro("Centro");
		enderecoEmpresa.setRua("Av. São João");
		enderecoEmpresa.setNumero("00");
		enderecoEmpresa.setCodigoPostal("01035-000");
		enderecoEmpresa = repositorioEndereco.save(enderecoEmpresa);

		Telefone telefoneEmpresa = new Telefone();
		telefoneEmpresa.setDdd("011");
		telefoneEmpresa.setNumero("986454527");
		telefoneEmpresa = repositorioTelefone.save(telefoneEmpresa);

		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Car service toyota ltda");
		empresa.setNomeFantasia("Car service manutenção veicular");
		empresa.setCadastro(LocalDateTime.now());
		empresa.setEndereco(enderecoEmpresa);
		empresa.getTelefones().add(telefoneEmpresa);
		empresa = repositorioEmpresa.save(empresa);

		Email emailFuncionario = new Email();
		emailFuncionario.setEndereco("a@a.com");
		emailFuncionario = repositorioEmail.save(emailFuncionario);

		Usuario funcionario = new Usuario();
		funcionario.setNome("Pedro Alcântara");
		funcionario.setNomeSocial("Dom Pedro");
		funcionario.getPerfis().add(PerfilUsuario.ROLE_VENDEDOR);
		funcionario.getEmails().add(emailFuncionario);
		funcionario.setEmpresa(empresa);
		funcionario = repositorioUsuario.save(funcionario);

		Email emailFornecedor = new Email();
		emailFornecedor.setEndereco("f@f.com");
		emailFornecedor = repositorioEmail.save(emailFornecedor);

		Usuario fornecedor = new Usuario();
		fornecedor.setNome("Fornecedor Auto");
		fornecedor.setNomeSocial("Loja do carro");
		fornecedor.getPerfis().add(PerfilUsuario.ROLE_VENDEDOR);
		fornecedor.getEmails().add(emailFornecedor);
		fornecedor.setEmpresa(empresa);
		repositorioUsuario.save(fornecedor);

		Mercadoria roda1 = new Mercadoria();
		roda1.setCadastro(LocalDateTime.now());
		roda1.setFabricacao(LocalDate.now());
		roda1.setNome("Roda Toyota");
		roda1.setValidade(LocalDate.now().plusYears(2));
		roda1.setQuantidade(29); // 30 em estoque - 1 vendida = 29
		roda1.setValor(new BigDecimal("300.00"));
		roda1.setDescricao("Original");
		roda1.setEmpresa(empresa);
		roda1 = repositorioMercadoria.save(roda1);

		Mercadoria roda2 = new Mercadoria();
		roda2.setCadastro(LocalDateTime.now());
		roda2.setFabricacao(LocalDate.now());
		roda2.setNome("Roda genérica");
		roda2.setValidade(LocalDate.now().plusYears(2));
		roda2.setQuantidade(29); // 30 em estoque - 1 vendida = 29
		roda2.setValor(new BigDecimal("150.00"));
		roda2.setDescricao("Segunda linha");
		roda2.setEmpresa(empresa);
		roda2 = repositorioMercadoria.save(roda2);

		Email emailCliente = new Email();
		emailCliente.setEndereco("cliente@exemplo.com");
		emailCliente = repositorioEmail.save(emailCliente);

		Usuario cliente = new Usuario();
		cliente.setNome("Cliente");
		cliente.getPerfis().add(PerfilUsuario.ROLE_CLIENTE);
		cliente.getEmails().add(emailCliente);
		cliente.setEmpresa(empresa);
		cliente = repositorioUsuario.save(cliente);

		Veiculo veiculo = new Veiculo();
		veiculo.setPlaca("ABC-0000");
		veiculo.setModelo("Corolla Cross");
		veiculo.setTipo(TipoVeiculo.SUV);
		veiculo.setProprietario(cliente);
		veiculo = repositorioVeiculo.save(veiculo);

		Servico s1 = new Servico();
		s1.setNome("Troca de rodas");
		s1.setValor(new BigDecimal("50.00"));
		s1.setEmpresa(empresa);
		s1 = repositorioServico.save(s1);

		Servico s2 = new Servico();
		s2.setNome("Alinhamento");
		s2.setValor(new BigDecimal("50.00"));
		s2.setEmpresa(empresa);
		s2 = repositorioServico.save(s2);

		Servico s3 = new Servico();
		s3.setNome("Balanceamento");
		s3.setValor(new BigDecimal("30.00"));
		s3.setEmpresa(empresa);
		s3 = repositorioServico.save(s3);

		// Venda 1 — 1x Roda Toyota + Troca de rodas + Alinhamento
		Venda v1 = new Venda();
		v1.setCadastro(LocalDateTime.now());
		v1.setIdentificacao("123");
		v1.setStatus(StatusVenda.FECHADA);
		v1.setClienteId(cliente.getId());
		v1.setClienteNomeSnapshot(cliente.getNome());
		v1.setFuncionarioId(funcionario.getId());
		v1.setFuncionarioNomeSnapshot(funcionario.getNome());
		v1.setVeiculoId(veiculo.getId());
		v1.setVeiculoPlacaSnapshot(veiculo.getPlaca());
		v1.setEmpresaId(empresa.getId());

		ItemVenda iv1 = new ItemVenda();
		iv1.setVenda(v1);
		iv1.setMercadoriaId(roda1.getId());
		iv1.setMercadoriaNomeSnapshot(roda1.getNome());
		iv1.setPrecoUnitarioSnapshot(roda1.getValor());
		iv1.setQuantidade(1);
		iv1.setSubtotal(roda1.getValor());
		v1.getItens().add(iv1);

		ItemServico is1 = new ItemServico();
		is1.setVenda(v1);
		is1.setServicoId(s1.getId());
		is1.setServicoNomeSnapshot(s1.getNome());
		is1.setPrecoSnapshot(s1.getValor());
		v1.getServicos().add(is1);

		ItemServico is2 = new ItemServico();
		is2.setVenda(v1);
		is2.setServicoId(s2.getId());
		is2.setServicoNomeSnapshot(s2.getNome());
		is2.setPrecoSnapshot(s2.getValor());
		v1.getServicos().add(is2);

		v1.setValorTotal(roda1.getValor().add(s1.getValor()).add(s2.getValor()));
		repositorioVenda.save(v1);

		// Venda 2 — 1x Roda genérica + Alinhamento + Balanceamento
		Venda v2 = new Venda();
		v2.setCadastro(LocalDateTime.now());
		v2.setIdentificacao("456");
		v2.setStatus(StatusVenda.FECHADA);
		v2.setClienteId(cliente.getId());
		v2.setClienteNomeSnapshot(cliente.getNome());
		v2.setFuncionarioId(funcionario.getId());
		v2.setFuncionarioNomeSnapshot(funcionario.getNome());
		v2.setVeiculoId(veiculo.getId());
		v2.setVeiculoPlacaSnapshot(veiculo.getPlaca());
		v2.setEmpresaId(empresa.getId());

		ItemVenda iv2 = new ItemVenda();
		iv2.setVenda(v2);
		iv2.setMercadoriaId(roda2.getId());
		iv2.setMercadoriaNomeSnapshot(roda2.getNome());
		iv2.setPrecoUnitarioSnapshot(roda2.getValor());
		iv2.setQuantidade(1);
		iv2.setSubtotal(roda2.getValor());
		v2.getItens().add(iv2);

		ItemServico is3 = new ItemServico();
		is3.setVenda(v2);
		is3.setServicoId(s2.getId());
		is3.setServicoNomeSnapshot(s2.getNome());
		is3.setPrecoSnapshot(s2.getValor());
		v2.getServicos().add(is3);

		ItemServico is4 = new ItemServico();
		is4.setVenda(v2);
		is4.setServicoId(s3.getId());
		is4.setServicoNomeSnapshot(s3.getNome());
		is4.setPrecoSnapshot(s3.getValor());
		v2.getServicos().add(is4);

		v2.setValorTotal(roda2.getValor().add(s2.getValor()).add(s3.getValor()));
		repositorioVenda.save(v2);
	}
}
